const {defineStore}=Pinia

const useQnaStore=defineStore('enrollment/qna',{
	state:()=>({
		member_id:0,
		showInsertForm:false,
		showUpdateForm:false,
		showDetail:false,
		qList:[],
		curQvo:{}, // 현재 선택된 질문 내용
		curRvo:{}, // 현재 선택된 답변 내용
		course_no:0,
		qCount:0,
		curpage:1,
		startpage:0,
		endpage:0,
		totalpage:0,
		newSubject:'',
		newContent:'',
		updateNo:0, // 현재 선택된 수정 게시물 번호
		updateSubject:'',
		updateContent:''
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
		dataRecv(res){
			console.log(res.data)
			this.qList=res.data.qList
			this.curpage=res.data.page
			this.totalpage=res.data.totalpage
			this.startpage=res.data.startpage
			this.endpage=res.data.endpage 
			this.qCount=res.data.qCount			
		},
		async courseQnaListData(course_no){
			this.course_no=course_no
			const res=await api.get('/enrollment/qna_vue',{
				params:{
					course_no:this.course_no,
					page:this.curpage
				}
			})
			this.dataRecv(res)
		},
		async toggleDetail(no){
			if(this.showDetail && this.curQvo.no===no){
			  this.showDetail=false
			  return
			}
			this.showDetail=false // 이전 상세 닫기
			const res=await api.get('/enrollment/qna_detail_vue',{
				params:{
					no:no
				}
			})
			console.log(res.data)
			if(!res.data.curQvo){
				alert("삭제된 게시글입니다")
				this.courseQnaListData(this.course_no)
				return
			}
			this.curQvo=res.data.curQvo
			if(this.curQvo.status==='Y'){
				const res2=await api.get('/enrollment/qna_reply_vue',{
					params:{
						course_qna_no:no
					}
				})
				console.log(res2.data)
				this.curRvo=res2.data.curRvo
			}
			this.showDetail=true
			this.showUpdateForm=false
		},
		move(page){
			this.curpage=page
			this.courseQnaListData(this.course_no)
		},
		toggleInsertForm(){
			this.showInsertForm=!this.showInsertForm
		},
		async courseQnaInsert(){
			if(this.newSubject===''){
				alert("제목을 입력해주세요")
				return
			}
			if(this.newContent===''){
				alert("내용을 입력해주세요")
				return
			}
			const res=await api.post('/enrollment/qna_insert_vue',{
				course_no:this.course_no,
				subject:this.newSubject,
				content:this.newContent
			})
			this.showInsertForm=false
			this.dataRecv(res)
			this.newSubject=''
			this.newContent=''
		},
		async courseQnaDelete(no){
			const res=await api.delete('/enrollment/qna_delete_vue',{
				params:{
					no:no,
					page:this.curpage,
					course_no:this.course_no
				}
			})
			//this.showInsertForm=false
			if(res.data.deleted==0){
				alert("답변이 등록되어 삭제할 수 없습니다...")
				return
			}
			this.dataRecv(res)
		},
		async toggleUpdate(no){
			if(this.showUpdateForm && this.updateNo===no){
			  this.showUpdateForm=false
			  return
			}
			this.showUpdateForm=false // 이전 수정폼 닫기
			this.updateNo=no
			const res=await api.get('/enrollment/qna_detail_vue',{
				params:{
					no:this.updateNo
				}
			})
			console.log(res.data)
			if(!res.data.curQvo){
				alert("삭제된 게시글입니다")
				this.courseQnaListData(this.course_no)
				return
			}
			this.updateNo=res.data.curQvo.no
			this.updateSubject=res.data.curQvo.subject
			this.updateContent=res.data.curQvo.content
			this.showUpdateForm=true
			this.showDetail=false
		},
		async courseQnaUpdate(){
			if(this.updateSubject===''){
				alert("제목을 입력해주세요")
				return
			}
			if(this.updateContent===''){
				alert("내용을 입력해주세요")
				return
			}
			const res=await api.put('/enrollment/qna_update_vue',{
				curpage:this.curpage,
				no:this.updateNo,
				course_no:this.course_no,
				subject:this.updateSubject,
				content:this.updateContent
			})
			if(res.data.updated==0){
				alert("답변이 등록되어 수정할 수 없습니다...")
				return
			}			
			this.showUpdateForm=false
			this.dataRecv(res)
			this.updateSubject=''
			this.updateContent=''
		}
	}
})