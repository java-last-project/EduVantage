const {defineStore}=Pinia

const useEvalStore=defineStore('enrollment/evaluation',{
	state:()=>({
		member_id:0,
		showUpdateForm:false,
		cvo:{},
		evo:null,
		course_no:0,
		star:0,
		rating:0,
		review:'',
		eList:[],
		eCount:0,
		liked:0,
		curpage:1,
		startpage:0,
		endpage:0,
		totalpage:0
	}),
	getters:{
		range:(state)=>{
			const arr=[]
			for(let i=state.startpage;i<=state.endpage;i++){
				arr.push(i)
			}
			return arr
		}
	},
	actions:{
		toggleUpdateForm(){
			this.showUpdateForm=!this.showUpdateForm
		},
		async courseData(course_no){
			//this.member_id=member_id
			this.course_no=course_no
			const res=await api.get('/enrollment/course_vue',{
				params:{
					course_no:this.course_no
				}
			})
			//console.log(res.data)
			this.cvo=res.data.vo
			this.star=res.data.vo.star
		},
		
		async myEvalData(){
			//this.member_id=member_id
			//this.course_no=course_no
			const res=await api.get('/enrollment/myevaluation_vue',{
				params:{
					course_no:this.course_no
				}
			})
			console.log(res.data.evo)
			this.evo=res.data.evo
			if(res.data.evo) {
				this.rating=res.data.evo.rating
				this.review=res.data.evo.review				
			}
			else {
				this.evo=null
				this.rating=0
				this.review=''			
			}

			this.showUpdateForm=false
		},
		dataRecv(res){
			console.log(res.data)
			this.eList=res.data.eList
			this.liked=res.data.liked
			this.curpage=res.data.page
			this.totalpage=res.data.totalpage
			this.startpage=res.data.startpage
			this.endpage=res.data.endpage 
			this.eCount=res.data.eCount			
			this.star=res.data.star
		},
		async evaluationListData(){
			//this.member_id=member_id
			//this.course_no=course_no
			const res=await api.get('/enrollment/evaluation_vue',{
				params:{
					page:this.curpage,
					course_no:this.course_no
				}
			})
			//this.myEvalData()
			this.dataRecv(res)
		},
		setRating(rating){
			this.rating=rating
			console.log('현재 별점: ',this.rating)
		},
		async evaluationInsert(){
			if(this.rating===0) {
				alert("별점을 선택해주세요")
				return
			}
			if(this.review==='') {
				alert("후기를 입력하세요")
				return
			}		
			const res=await api.post('/enrollment/evaluation_insert_vue',{
				course_no:this.course_no,
				rating:this.rating,
				review:this.review
			})
			this.dataRecv(res)
			this.myEvalData()
			
			this.rating=0
			this.review=''
		},
		async evaluationDelete(ce_no){
			const res=await api.delete('/enrollment/evaluation_delete_vue',{
				params:{
					ce_no:ce_no,
					course_no:this.course_no,
					curpage:this.curpage
				}
			})
			this.dataRecv(res)
			this.myEvalData()
		},
		async evaluationUpdate(){
			if(this.rating===0) {
				alert("별점을 선택해주세요")
				return
			}
			if(this.review==='') {
				alert("후기를 입력하세요")
				return
			}	
			const res=await api.put('/enrollment/evaluation_update_vue',{
				no:this.evo.no,
				rating:this.rating,
				review:this.review,
				course_no:this.course_no,
				curpage:this.curpage
			})
			this.myEvalData()
			this.dataRecv(res)
		},
		async evaluationLikeOn(ce_no){
			const res=await api.post('/enrollment/evaluation_likeon_vue',{
				ce_no:ce_no,
				course_no:this.course_no,
				curpage:this.curpage
			})
			this.dataRecv(res)
		},
		async evaluationLikeOff(ce_no){
			const res=await api.delete('/enrollment/evaluation_likeoff_vue',{
				params:{
					ce_no:ce_no,
					course_no:this.course_no,
					curpage:this.curpage
				}
			})
			this.dataRecv(res)
		},		
		move(page){
			this.curpage=page
			this.evaluationListData()
		}

	}
})