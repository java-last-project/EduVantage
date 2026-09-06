(function(){
const { createApp, ref, onMounted } = Vue;


const scheduledExamApp = createApp({
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

        //알림등록
        const examNotiRegister = async (exam) => {
            try{
                await api.post("/notification/exam/subscribe",{examNo: exam.no})
                showToast("default", "정기 시험 알림 구독", exam.title+" 시험이 다가오면 알려드릴게요")

                //알림목록 새로고침
                const store = useNotificationStore()
                await store.fetchNotifications()
            }catch(error){
                console.error(error)
            }
        }

        onMounted(()=>{
            scheduledExamListData()
        })

        return {sList,year,month, page, totalpage, prevMonth, nextMonth, prevPage, nextPage, examNotiRegister}
    }
})

scheduledExamApp.mount("#scheduled-exam-list")
})()