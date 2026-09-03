/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    list:[],
	fd:'',
    curpage:1,
	totalpage:0,
	startPage:0,
	endPage:0,
	count:0,
})
const useFreeboardStore=defineStore('freeboard_store',{
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
        async freeboardListData(params){
			try{
	            const res=await api.get('/freeboard/list_vue', {
					params:{
		                page: this.curpage,
						fd: this.fd
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
			await this.freeboardListData()
		}
	}
})