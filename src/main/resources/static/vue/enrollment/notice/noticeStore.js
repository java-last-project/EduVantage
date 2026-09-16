const {defineStore}=Pinia

const useNoticeStore=defineStore('enrollment/notice',{
	state:()=>({
		member_id:0,
		showDetail:false,
		nList:[],
		curNvo:{},
		course_no:0,
		nCount:0,
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
		dataRecv(res){
			console.log(res.data)
			this.nList=res.data.nList
			this.curpage=res.data.page
			this.totalpage=res.data.totalpage
			this.startpage=res.data.startpage
			this.endpage=res.data.endpage 
			this.nCount=res.data.nCount			
		},
		async courseNoticeListData(course_no){
			this.course_no=course_no
			const res=await api.get('/enrollment/notice_vue',{
				params:{
					course_no:this.course_no,
					page:this.curpage
				}
			})
			this.dataRecv(res)
		},
		async toggleDetail(no){
			if(this.showDetail && this.curNvo.no===no){
			  this.showDetail=false
			  return
			}
			this.showDetail=false // 이전 상세 닫기
			const res=await api.get('/enrollment/notice_detail_vue',{
				params:{
					no:no
				}
			})
			console.log(res.data)
			if(!res.data.curNvo){
				alert("삭제된 게시글입니다")
				this.courseNoticeListData(this.course_no)
				return
			}
			this.curNvo=res.data.curNvo
			this.showDetail=true
		},
		move(page){
			this.curpage=page
			this.courseNoticeListData(this.course_no)
		}
	}
})