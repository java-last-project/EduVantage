/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    list:[],
    theme:0,
    count:1,
	enrollmentNo:null,
	username:'',
	examNo:null,
	title:''
})
const useExamStore=defineStore('exam_store',{
    state:initialState,
    actions:{
        async examDetailData(params){
			if (params) {
				this.theme=params.theme ?? this.theme
				this.count=params.count ?? this.count
				this.examNo=params.examNo ?? this.examNo
			}
			try{
	            const res=await api.post('/exam/detail_vue', {
	                theme: this.theme,
	                count: this.count,
					examNo: this.examNo
	            })
	            this.enrollmentNo=res.data.enrollmentNo
				this.title=res.data.title
	            this.list=res.data.list
			}catch(error){
			console.error(error)
			}
		}
	}
})