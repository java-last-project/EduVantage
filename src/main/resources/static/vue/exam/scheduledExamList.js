const { createApp, ref, onMounted } = Vue

//ref의 역할

createApp({
    setup(){
        //정기시험 목록
        const sList = ref([])

        //params
        const year = ref(new Date().getFullYear())
        console.log(year.value)
        const month = ref(new Date().getMonth()+1)
        console.log(month.value)

        //서버에 시험 목록 요청하는 함수
        const scheduledExamListData = async (params) => {
            if(params){
                year.value = params.year
                month.value = params.month
            }
            try{
               const res = await api.get('/scheduled-exam',{
                   params: {year: year.value, month:month.value}
               })
                sList.value = res.data
                console.log(sList)
            }catch (error){
                console.error(error)
            }
        }
        onMounted(()=>{
            scheduledExamListData()
        })

        return {sList,year,month}
    }
}).mount("#scheduled-exam-list")