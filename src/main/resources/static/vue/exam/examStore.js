/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    list:[],
    theme:0,
    count:1,
	enrollmentNo:null,
	username:'',
	examNo:null,
	title:'',
	startTime:null,
	timeLimitMinutes:120
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
					examNo: this.examNo,
					ai: params?.ai ?? false,
					enrollmentNo: params?.enrollmentNo ?? null,
					examName: params?.examName ?? null
	            })
	            this.enrollmentNo=res.data.enrollmentNo
				this.title=res.data.title
				this.startTime=res.data.startTime
				this.timeLimitMinutes=res.data.timeLimitMinutes || 120
	            this.list=res.data.list
			}catch(error){
				console.error(error)
				throw error
			}
		}
	}
})
