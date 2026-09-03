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

        const page = ref(0)
        const totalpage = ref(0)


        //서버에 시험 목록 요청하는 함수
        const scheduledExamListData = async (params) => {
            if(params){
                year.value = params.year
                month.value = params.month
                page.value = params.page
            }
            try{
               const res = await api.get('/scheduled-exam',{
                   params: {year: year.value, month:month.value, page:page.value}
               })
                sList.value = res.data.content
                totalpage.value = res.data.totalPages
                console.log(sList)
            }catch (error){
                console.error(error)
            }
        }
        //이전 달 시험목록 조회
        const prevMonth = () => {
            if(month.value === 1){
                year.value -= 1
                month.value = 12
            }else{
                month.value -= 1
            }
            page.value=0
            scheduledExamListData()
        }
        //다음 달 시험목록 조회
        const nextMonth = () => {
            if(month.value === 12){
                year.value += 1
                month.value = 1
            }else{
                month.value += 1
            }
            page.value=0
            scheduledExamListData()
        }
        //이전 페이지
        const prevPage = () => {
            if(page.value <= 0) return
            page.value -= 1
            scheduledExamListData()
        }
        //다음 페이지
        const nextPage = () => {
            if(page.value >= totalpage.value) return
            page.value += 1
            scheduledExamListData()
        }

        onMounted(()=>{
            scheduledExamListData()
        })

        return {sList,year,month, page, totalpage, prevMonth, nextMonth, prevPage, nextPage}
    }
}).mount("#scheduled-exam-list")