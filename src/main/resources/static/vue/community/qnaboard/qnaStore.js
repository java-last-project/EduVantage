/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    list:[],
	fd:'',
	category:null,
    curpage:1,
	totalpage:0,
	startPage:0,
	endPage:0,
	count:0,
	categoryList:[
		{no:null, name:'전체'},
		{no:1, name:'계정/인증'},
		{no:2, name:'결제/환불'},
		{no:3, name:'사이트 이용장애'},
		{no:5, name:'쿠폰/이벤트'},
		{no:10, name:'기타 문의'}
	]
})
const useQnaStore=defineStore('qna_store',{
    state:initialState,
	getters:{
		range:(state)=>{
			const arr=[]
			for(let i=state.startPage;i<=state.endPage;i++){
				arr.push(i)
			}
			return arr
		}
	},
    actions:{
        async qnaListData(params){
			try{
	            const res=await api.get('/qna/list_vue', {
					params:{
		                page: this.curpage,
						fd: this.fd,
						categoryNo: this.category
					}
	            })
	            console.log(res.data)
	            this.list=res.data.list
				this.curpage=res.data.curpage
				this.totalpage=res.data.totalpage
				this.startPage=res.data.startPage
				this.endPage=res.data.endPage
				this.count=res.data.count
			}catch(error){
			console.error(error)
			}
		},
		async move(page){
			this.curpage=page
			await this.qnaListData()
		},
		async setCategory(category){
			this.category=category
			this.curpage=1
			await this.qnaListData()
		}
	}
})