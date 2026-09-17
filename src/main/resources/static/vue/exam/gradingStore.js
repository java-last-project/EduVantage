/** @type {import('pinia')}*/
const {defineStore}=Pinia
const useExamGradingStore=defineStore('examGradingStore',{
    state:()=>({
        answers:[],
        selectedAnswer:null,
        score:0,
        loading:false,
        submitting:false,
        errorMessage:''
    }),

    getters:{
        unclaimedCount:(state)=>{
            return state.answers.filter(answer=>!answer.grader_id).length
        },

        claimedCount:(state)=>{
            /*
             * 조회 API가 미선점 답안 또는 현재 강사가 선점한 답안만 반환하므로
             * grader_id가 존재하는 항목은 현재 강사가 선점한 답안이다.
             */
            return state.answers.filter(answer=>answer.grader_id).length
        }
    },

    actions:{
		isPracticeExam(answer){
			return answer?.exam_no==null && answer?.theme!=null
		},
        async loadAnswers(){
            const selectedAnswerNo=this.selectedAnswer?.answer_no

            this.loading=true
            this.errorMessage=''

            try{
                const response=await api.get('/instructor/exam/grading/pending')
                this.answers=response.data

                if(selectedAnswerNo){
                    this.selectedAnswer=this.answers.find(
                        answer=>answer.answer_no===selectedAnswerNo
                    )||null
                }
            }catch(error){
                console.error(error)
                this.handleError(
                    error,
                    '채점 대기 답안을 불러오지 못했습니다.'
                )
            }finally{
                this.loading=false
            }
        },

        selectAnswer(answer){
            this.selectedAnswer=answer
            this.score=0
            this.errorMessage=''
        },

        async claimAnswer(){
            if(!this.selectedAnswer){
                return
            }

            const answerNo=this.selectedAnswer.answer_no

            this.submitting=true
            this.errorMessage=''

            try{
                await api.post(`/instructor/exam/grading/${answerNo}/claim`)

				// 선점 후 서버 상태 재조회
                await this.loadAnswers()

                this.selectedAnswer=this.answers.find(answer=>answer.answer_no===answerNo)||null
            }catch(error){
                console.error(error)

                if(error.response?.status===409){
					// 동시 선점 실패는 409 Conflict로 구분
                    this.errorMessage='다른 강사가 먼저 선점한 답안입니다.'
                }else{
                    this.handleError(error, '답안을 선점하지 못했습니다.')
                }
                await this.loadAnswers()
            }finally{
                this.submitting=false
            }
        },

        async releaseAnswer(){
            if(!this.selectedAnswer){
                return
            }

            const answerNo=this.selectedAnswer.answer_no

            this.submitting=true
            this.errorMessage=''

            try{
                await api.delete(
                    `/instructor/exam/grading/${answerNo}/claim`
                )

                await this.loadAnswers()

                this.selectedAnswer=this.answers.find(
                    answer=>answer.answer_no===answerNo
                )||null
            }catch(error){
                console.error(error)
                this.handleError(error, '답안 선점을 해제하지 못했습니다.')
            }finally{
                this.submitting=false
            }
        },

        async submitGrade(correct){
            if(!this.selectedAnswer?.grader_id){
                this.errorMessage='답안을 먼저 선점해야 합니다.'
                return
            }

            const practiceExam=this.isPracticeExam(this.selectedAnswer)
            let payload
            let confirmMessage
            if(practiceExam){
				// 상시시험은 문제은행 배점 대신 정오 판정만 전달
                if(typeof correct!=='boolean'){
                    this.errorMessage='정답 또는 오답을 선택해 주세요.'
                    return
                }
                payload={correct}
                confirmMessage=`${correct?'정답':'오답'}으로 채점을 완료하시겠습니까?`
            }else{
                const maxScore=Number(this.selectedAnswer.score)
                const inputScore=Number(this.score)

				// 문항 배점 범위 + 정수 검증
                if(
                    !Number.isInteger(inputScore) ||
                    inputScore<0 ||
                    inputScore>maxScore
                ){
                    this.errorMessage=
                        `점수는 0점부터 ${maxScore}점 사이의 정수여야 합니다.`
                    return
                }
                payload={score:inputScore}
                confirmMessage=`${inputScore}점으로 채점을 완료하시겠습니까?`
            }

            const confirmed=confirm(confirmMessage)

            if(!confirmed){
                return
            }

            this.submitting=true
            this.errorMessage=''

            try{
                await api.post(
                    `/instructor/exam/grading/${this.selectedAnswer.answer_no}/grade`,
                    payload
                )
                this.selectedAnswer=null
                this.score=0
                await this.loadAnswers()
            }catch(error){
                console.error(error)
                this.handleError(
                    error,
                    '채점 결과를 저장하지 못했습니다.'
                )
            }finally{
                this.submitting=false
            }
        },
        handleError(error,defaultMessage){
            const status=error.response?.status
            if(status===401){
                this.errorMessage='로그인이 필요합니다.'
                return
            }
            if(status===403){
                this.errorMessage='강사만 접근할 수 있는 기능입니다.'
                return
            }
            this.errorMessage=defaultMessage
        }
    }
})