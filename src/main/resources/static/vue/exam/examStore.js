/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    list:[],
    type:0,
    count:1
})
const useExamStore=defineStore('exam_store',{
    state:initialState,
    actions:{
        async examDetailData(params){
			if (params) {
				this.type = params.type;
				this.count = params.count;
			}
			try{
	            const res=await api.post('/exam/detail_vue', {
	                type: this.type,
	                count: this.count
	            })
	            console.log(res.data)
	            this.list=res.data.list
			}catch(error){
			console.error(error)
			}
		}
	}
})