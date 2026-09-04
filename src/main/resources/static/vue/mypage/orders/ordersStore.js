const {defineStore}=Pinia

const useOrdersStore=defineStore('mypage/orders',{
	state:()=>({
		member_id:0,
		activeTab:'course',
		cList:[],
		cCount:0,
		bCount:0,
		bList:[],
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
		setTab(tab){
			this.activeTab=tab
			this.curpage=1
			if(tab==='course'&&this.cList.length===0) {
				this.coursePaymentListData(this.member_id)
			}
			else if(tab==='book'&&this.bList.length===0) {
				this.booksOrderListData(this.member_id)
			}
		},
		async coursePaymentListData(member_id){
			this.member_id=member_id
			const res=await api.get('/mypage/course_orders_vue',{
				params:{
					page:this.curpage,
					member_id:this.member_id
				}
			})
			console.log(res.data)
			this.cList=res.data.cList
			this.curpage=res.data.page
			this.totalpage=res.data.totalpage
			this.startpage=res.data.startpage
			this.endpage=res.data.endpage 
			this.cCount=res.data.cCount
		},
		async booksOrderListData(member_id){
			this.member_id=member_id
			const res=await api.get('/mypage/book_orders_vue',{
				params:{
					page:this.curpage,
					member_id:this.member_id
				}
			})
			console.log(res.data)
			this.bList=res.data.bList
			this.curpage=res.data.page
			this.totalpage=res.data.totalpage
			this.startpage=res.data.startpage
			this.endpage=res.data.endpage
		},		
		moveCourse(page){
			this.curpage=page
			this.coursePaymentListData(this.member_id)
		},
		moveBook(page){
			this.curpage=page
			this.booksOrderListData(this.member_id)
		}
		//toggleProfileForm(){
		//	this.showProfileForm=!this.showProfileForm
		//}
	}
})