/** @type {import('pinia')}*/
const Pinia=window.Pinia

const {defineStore}=Pinia
const initailState=()=>({
    list:[],
    type:0,
    count:1
})
const useExamStore=defineStore('exam_store',{
    state:initailState(),
    actions:{
        async examDetailData(){
            const res=await api.post('/exam/detail_vue', {
                type: this.type,
                count: this.count
            })
            console.log(res.data)
            this.list=res.data.list
        }
    }
})