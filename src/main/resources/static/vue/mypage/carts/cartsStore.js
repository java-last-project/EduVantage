const {defineStore}=Pinia

const useCartsStore=defineStore('mypage/carts',{
	state:()=>({
		impCode:'',
		member_id:0,
		member:{},
		activeTab:'course',
		cList:[],
		cCount:0,
		bCount:0,
		bList:[],
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
		},
		activeList(state){
		    return state.activeTab==='course' ? state.cList : state.bList
		},
		selectedItems(){
		    return this.activeList.filter(a=>a.selected)
		},
		selectedCount(){
		    return this.selectedItems.length
		},
		selectedTotal(){
		    return this.selectedItems.reduce((sum,a)=> sum + (a.pay_price ?? a.price * (a.quantity ?? 1)), 0)
		},
		isAllSelected(){
		    return this.activeList.length>0 && this.activeList.every(a=>a.selected)
		}
	},
	actions:{
		setTab(tab){
			this.activeTab=tab
			if(tab==='course'&&this.cList.length===0) {
				this.cCurpage=1
				this.courseCartListData(this.member_id)
			}
			else if(tab==='book'&&this.bList.length===0) {
				this.bCurpage=1
				this.booksCartListData(this.member_id)
			}
		},
		toggleCourseAll(checked){
			this.cList.forEach(c=>c.selected=checked)
		},
		toggleBookAll(checked){
			this.bList.forEach(b=>b.selected=checked)
		},
		async deleteSelectedCourse(){
			if(this.selectedCount===0) return
			if(!confirm("삭제하시겠습니까?")) return
			await api.delete('/mypage/course_cart/delete',{
				params:{
					course_list: this.selectedItems.map(a => a.course_no)
				}
			})
			this.cCurpage=1
			this.courseCartListData(this.member_id)
		},
		async deleteSelectedBook(){
			if(this.selectedCount===0) return
			if(!confirm("삭제하시겠습니까?")) return
			await api.delete('/mypage/book_cart/delete',{
				params:{
					book_list: this.selectedItems.map(a => a.book_no)
				}
			})
			this.bCurpage=1
			this.booksCartListData(this.member_id)
		},
		async courseCartListData(member_id){
			this.member_id=member_id
			const res=await api.get('/mypage/course_carts_vue',{
				params:{
					page:this.cCurpage,
					member_id:this.member_id
				}
			})
			console.log(res.data)
			this.cList=res.data.cList.map(c=>({...c,selected:false}))
			this.cCurpage=res.data.page
			this.cTotalpage=res.data.totalpage
			this.cStartpage=res.data.startpage
			this.cEndpage=res.data.endpage 
			this.cCount=res.data.cCount
		},
		async booksCartListData(member_id){
			this.member_id=member_id
			const res=await api.get('/mypage/book_carts_vue',{
				params:{
					page:this.bCurpage
				}
			})
			console.log(res.data)
			this.bList=res.data.bList.map(b=>({...b,selected:false}))
			this.bCurpage=res.data.page
			this.bTotalpage=res.data.totalpage
			this.bStartpage=res.data.startpage
			this.bEndpage=res.data.endpage
			this.bCount=res.data.bCount
		},		
		moveCourse(page){
			this.cCurpage=page
			this.courseCartListData(this.member_id)
		},
		moveBook(page){
			this.bCurpage=page
			this.booksCartListData(this.member_id)
		},
		async requestPay(){
			const IMP=window.IMP
			
			const memberData=await api.get('/member/info_vue')
			//console.log(memberData.data)
			
			IMP.init(this.impCode)
			
			IMP.request_pay({
				pg: "html5_inicis.INIpayTest",
				pay_method: "card",
				merchant_uid: "COURSE_"+new Date().getTime(),
				name: this.selectedCount > 1 ? `${this.selectedItems[0].title} 외 ${this.selectedCount - 1}건` : this.selectedItems[0].title,
				amount: this.selectedTotal,
				buyer_email: memberData.data.email,
				buyer_name: memberData.data.name,
				buyer_tel: memberData.data.phone
			}, async(rsp)=>{
			/*	if(rsp.success){
					alert("결제가 성공했습니다")
				} else {
					alert("결제가 취소되었습니다")
				} */
				await api.post('/mypage/course_cart/checkout',{
					course_list: this.selectedItems.map(c=>c.course_no)
				})
				if(confirm('결제가 완료되었습니다! 내 학습으로 이동하시겠습니까?')){
					location.href="/mypage/courses"
				}
			})		
		},
		goToBookCheckout(bvo){
		    location.href = `/book/checkout?bookNo=${bvo.book_no}&quantity=${bvo.quantity}`
		}
	}
})