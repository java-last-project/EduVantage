const {defineStore}=Pinia

const useDashboardStore=defineStore('mypage/dashboard',{
	state:()=>({
		nearestExam: null,
		examDday: null,
		examLoaded: false,
		rList:[],
		recommendLoaded: false
	}),
	actions:{
		async getNearestExam(){
			const now=new Date()
			try {
				const res=await api.get('/scheduled-exam',{
					params:{
						year:now.getFullYear(),
						month:now.getMonth()+1,
						page:0
					}
				})
				const list=res.data.content
				if(!list || list.length===0){
					this.nearestExam=null
					this.examDday=null
				}
				else {
					const upcoming = list.filter(exam => new Date(exam.close_date) >= now)
					if(upcoming.length === 0){
					      this.nearestExam=null
					      this.examDday=null
					}
					else {
						const exam=upcoming[0]
						this.nearestExam=exam
						if(new Date(exam.open_date) <= now){
							this.examDday=0
						}
						else{
						    this.examDday=Math.ceil((new Date(exam.open_date)-now)/(1000*60*60*24))
						}
					}
				}
			} catch(error) {
				console.log(error)
				this.nearestExam=null
				this.examDday=null
			} finally {
				this.examLoaded=true
			}
		},
		goExam(){
			if(this.examDday === 0){
				if(confirm("시험에 응시하시겠습니까?")) {
					location.href = '/exam/detail?examNo=' + this.nearestExam.no
				}
			} else {
				location.href = '/exam/list'
			}
		},
		async getRecommendCourses(){
			try {
				const res=await api.get('/mypage/recommend_courses')
				console.log(res.data)
				this.rList=res.data.rList
			} catch(error){
				console.log(error)
				this.rList=[]
			} finally {
				this.recommendLoaded=true
			}
		}
	}
})