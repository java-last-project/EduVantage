const {defineStore}=Pinia

const useOrdersStore=defineStore('mypage/orders',{
	state:()=>({
		member_id:0,
		activeTab:'course',
		cList:[],
		cCount:0,
		cTotalCount:0,
		bCount:0,
		bTotalCount:0,
		bList:[],
		cOrderStatus:'',
		bOrderStatus:'',
		cCurpage:1,
		cStartpage:0,
		cEndpage:0,
		cTotalpage:0,
		bCurpage:1,
		bStartpage:0,
		bEndpage:0,
		bTotalpage:0		
	}),
	getters:{
		range:(state)=>(start,end)=>{
			const arr=[]
			for(let i=start;i<=end;i++){
				arr.push(i)
			}
			return arr
		}
	},
	actions:{
		setTab(tab){
			this.activeTab=tab
			if(tab==='course'&&this.cList.length===0) {
				this.cCurpage=1
				this.coursePaymentListData(this.member_id)
			}
			else if(tab==='book'&&this.bList.length===0) {
				this.bCurpage=1
				this.booksOrderListData(this.member_id)
			}
		},
		changecoursePaymentListStatus(status){
			this.cOrderStatus=status
			this.cCurpage=1
			this.coursePaymentListData(this.member_id)
		},
		async coursePaymentListData(member_id){
			this.member_id=member_id
			const res=await api.get('/mypage/course_orders_vue',{
				params:{
					page:this.cCurpage,
					member_id:this.member_id,
					order_status:this.cOrderStatus
				}
			})
			console.log(res.data)
			this.cList=res.data.cList
			this.cCurpage=res.data.page
			this.cTotalpage=res.data.totalpage
			this.cStartpage=res.data.startpage
			this.cEndpage=res.data.endpage 
			this.cCount=res.data.cCount
			this.cTotalCount=res.data.cTotalCount
		},
		changeBookOrderListStatus(status){
			this.bOrderStatus=status
			this.bCurpage=1
			this.booksOrderListData(this.member_id)
		},
		bookDataRecv(res){
			console.log(res.data)
			this.bList=res.data.bList
			this.bCurpage=res.data.page
			this.bTotalpage=res.data.totalpage
			this.bStartpage=res.data.startpage
			this.bEndpage=res.data.endpage
			this.bCount=res.data.bCount
		},
		async booksOrderListData(member_id){
			this.member_id=member_id
			const res=await api.get('/mypage/book_orders_vue',{
				params:{
					page:this.bCurpage,
					member_id:this.member_id,
					order_status:this.bOrderStatus
				}
			})
			this.bookDataRecv(res)
			this.bTotalCount=res.data.bTotalCount
		},
		async booksChangeWaitRefundState(no){
			//this.member_id=member_id
			const res=await api.put('/mypage/book_wait_refund_vue',{},{
				params:{
					page:this.bCurpage,
					no:no,
					order_status:this.bOrderStatus
				}
			})
			this.bookDataRecv(res)
			alert("환불 요청 처리 되었습니다.")
		},
		moveCourse(page){
			this.cCurpage=page
			this.coursePaymentListData(this.member_id)
		},
		moveBook(page){
			this.bCurpage=page
			this.booksOrderListData(this.member_id)
		}
	}
})