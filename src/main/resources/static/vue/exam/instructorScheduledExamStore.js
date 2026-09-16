/** @type {import('pinia')}*/
const {defineStore}=Pinia
const useInstructorScheduledExamStore=defineStore('instructorScheduledExamStore',{
	state:()=>({
		exams:[],
		questions:[],
		form:{
			no:null,
			title:'',
			open_date:'',
			close_date:'',
			time_limit:120,
			question_nos:[]
		},
		theme:0,
		search:'',
		showForm:false,
		editingStarted:false,
		loading:false,
		saving:false,
		errorMessage:''
	}),

	getters:{
		filteredQuestions(state){
			const keyword=state.search.trim().toLowerCase()
			return state.questions.filter(question=>{
				const matchesTheme=!state.theme || question.theme===state.theme
				const matchesKeyword=!keyword || String(question.title||'').toLowerCase().includes(keyword)
				return matchesTheme && matchesKeyword
			})
		},
		selectedScore(state){
			return state.questions
				.filter(question=>state.form.question_nos.includes(question.no))
				.reduce((sum,question)=>sum+Number(question.score||0),0)
		}
	},

	actions:{
		async loadExams(){
			this.loading=true
			this.errorMessage=''
			try{
				const res=await api.get('/instructor/exam/scheduled/list')
				this.exams=res.data || []
			}catch(error){
				this.handleError(error,'정기시험 목록을 불러오지 못했습니다.')
			}finally{
				this.loading=false
			}
		},

		async loadQuestions(){
			try{
				const res=await api.get('/instructor/exam/scheduled/questions')
				this.questions=res.data || []
			}catch(error){
				this.handleError(error,'문제은행을 불러오지 못했습니다.')
			}
		},

		async openCreate(){
			this.resetForm()
			this.showForm=true
			await this.loadQuestions()
		},

		async openEdit(exam){
			this.errorMessage=''
			try{
				const res=await api.get(`/instructor/exam/scheduled/${exam.no}`)
				const data=res.data
				this.form={
					no:data.no,
					title:data.title,
					open_date:this.toDateTimeInput(data.open_date),
					close_date:this.toDateTimeInput(data.close_date),
					time_limit:data.time_limit || 120,
					question_nos:data.question_nos || []
				}
				this.editingStarted=new Date(data.open_date)<=new Date()
				this.showForm=true
				await this.loadQuestions()
			}catch(error){
				this.handleError(error,'정기시험 정보를 불러오지 못했습니다.')
			}
		},

		async saveExam(){
			this.errorMessage=''
			if(!this.form.title.trim()){
				this.errorMessage='시험명을 입력해주세요.'
				return
			}
			if(!this.form.open_date || !this.form.close_date){
				this.errorMessage='응시 시작일과 종료일을 입력해주세요.'
				return
			}
			if(!this.editingStarted && this.selectedScore!==100){
				this.errorMessage='선택한 문제의 배점 합계가 100점이어야 합니다.'
				return
			}

			this.saving=true
			try{
				if(this.form.no){
					await api.put(`/instructor/exam/scheduled/${this.form.no}`,this.form)
				}else{
					await api.post('/instructor/exam/scheduled',this.form)
				}
				this.showForm=false
				this.resetForm()
				await this.loadExams()
			}catch(error){
				this.handleError(error,'정기시험을 저장하지 못했습니다.')
			}finally{
				this.saving=false
			}
		},

		async deleteExam(exam){
			if(!confirm(`'${exam.title}' 시험을 삭제하시겠습니까?`)) return
			this.errorMessage=''
			try{
				await api.delete(`/instructor/exam/scheduled/${exam.no}`)
				await this.loadExams()
			}catch(error){
				this.handleError(error,'정기시험을 삭제하지 못했습니다.')
			}
		},

		closeForm(){
			this.showForm=false
			this.resetForm()
		},

		resetForm(){
			this.form={
				no:null,
				title:'',
				open_date:'',
				close_date:'',
				time_limit:120,
				question_nos:[]
			}
			this.theme=0
			this.search=''
			this.editingStarted=false
			this.errorMessage=''
		},

		toDateTimeInput(value){
			if(!value) return ''
			return String(value).slice(0,16)
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
			this.errorMessage=error.response?.data?.detail || error.response?.data?.message || defaultMessage
		}
	}
})
