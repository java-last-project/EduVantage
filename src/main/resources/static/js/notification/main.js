//header에 알림모듈(로그인 상태)이 있는 경우에만 연결
const badge = document.getElementById("notification-badge")

if(badge){
    //[WHAT] EventSource: 브라우저 내장객체, 서버->클라이언트 데이터 통로 생성
    //[변경예정] 세션기반에 따라서 쿠키에 JSESSIONID를 실어보내도록 설정 (요청 구분)
    const es = new EventSource("/sse", {withCredentials: true})

    // 서버에서 데이터를 보낼 때(emitter.send())마다 실행
    es.onmessage = (e) => {
        const data = JSON.parse(e.data)
        //알림 뱃지 노출
        document.getElementById("notification-badge").style.display = "inline-block"
        //토스트 알림
        showToast(data.type, data.title, data.content)
    }

    //sse 연결끊겼을 경우
    es.onerror = () => {
        console.log("sse 연결 끊김")
    }
}

function showToast(type, title, content){
    const toast = document.createElement("div")
    toast.className = "sse-toast"

    const titleSectionEl = document.createElement("div")
    titleSectionEl.className = "d-flex gap-2 d-inline-flex align-items-center justify-content-center"
    const badgeEl = document.createElement("span")
    badgeEl.className = "position-relative text-decoration-none px-1 py-1 rounded-circle"
    badgeEl.style.width = "32px"
    badgeEl.style.height = "32px"
    const iconEl = document.createElement("i")
    if(type === "COURSE_COMPLETED"){
        badgeEl.classList.add("text-success","bg-success-subtle")
        iconEl.className = "fa-solid fa-award"
    }



    const titleEl = document.createElement("strong")
    titleEl.textContent = title

    const contentEl = document.createElement("p")
    contentEl.textContent = content

    badgeEl.appendChild(iconEl)
    titleSectionEl.append(badgeEl,titleEl)

    toast.appendChild(titleSectionEl)
    toast.appendChild(contentEl)

    document.body.appendChild(toast);

    setTimeout(()=>{
        //페이드 적용안됨 오류 [수정필요]
        toast.classList.add("fade-out")
        setTimeout(()=> toast.remove(), 300)
    }, 6000)
}


const notificationList = createApp({
    setup(){

        const nnList = ref([])
        //최근 3일의 알림 조회
        const nnListData = async () => {
            try {
                const res = await api.get("/notification")
                console.log(res)
                nnList.value = res.data
            }catch(error){
                console.error(error)
            }
        }
        onMounted(()=>{
            nnListData()
        })

        return {nnList}
    }
}).mount("#notificationModal")