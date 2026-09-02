/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    cList:[],
    categories:[],
	column:'',
	fd:'',
    curpage:1,
	totalpage:0,
	startPage:0,
	endPage:0,
	count:0,
	catList:[]
})
const useCourseStore=defineStore('course_store',{
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
        async courseListData(params){
			if (params) {
				this.column=params.column
				this.categories=params.categories
				this.curpage=params.page
			}
			try{
	            const res=await api.get('/course/list_vue', {
					params:{
		                column: this.column,
						category: this.categories.join(','),
		                page: this.curpage,
						fd: this.fd
					}
	            })
	            console.log(res.data)
	            this.cList=res.data.cList
				this.catList=res.data.catList
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
			await this.courseListData()
		}
	}
})