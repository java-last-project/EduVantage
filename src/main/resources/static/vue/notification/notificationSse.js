//header에 알림모듈이 있는 상태(로그인된 상태)에만 연결
const badge = document.getElementById("notification-badge")

if(badge){
    //[WHAT] EventSource: 브라우저 내장객체, 서버->클라이언트 데이터 통로 생성
    //[변경예정] 세션기반에 따라서 쿠키에 JSESSIONID를 실어보내도록 설정 (요청 구분)
    const es = new EventSource("/sse", {withCredentials: true})

    // 서버에서 데이터를 보낼 때(emitter.send())마다 실행
    es.onmessage = async (e) => {
        const data = JSON.parse(e.data)
        // 인자 없이 호출하면 그 시점의 전역 active pinia(다른 페이지 앱이 마지막에 설정한 것)를
        // 잘못 참조할 수 있어 notificationPinia를 명시적으로 지정함
        const store = useNotificationStore(notificationPinia)
        store.addNotification(data)

        //알림목록 새로고침
        showToast(data.type, data.title, data.content, data.related_id)
        await store.fetchNotifications()
    }

    es.onerror = () => {
        console.log("sse 연결 끊김")
    }
}

function showToast(type, title, content, related_id) {
    const toast = document.createElement("div")
    toast.className = "sse-toast"

    const titleSectionEl = document.createElement("div")
    titleSectionEl.className = "d-flex gap-2 d-inline-flex align-items-center justify-content-center"
    const badgeEl = document.createElement("span")
    badgeEl.className = "position-relative text-decoration-none px-1 py-1 rounded-circle"
    badgeEl.style.width = "32px"
    badgeEl.style.height = "32px"
    const iconEl = document.createElement("i")
    if (type === "COURSE_COMPLETED") {
        badgeEl.classList.add("text-success", "bg-success-subtle")
        iconEl.className = "fa-solid fa-award"
    }
    if (type === "EXAM_SUBSCRIBED") {
        badgeEl.classList.add("text-info", "bg-info-subtle")
        iconEl.className = "fa-solid fa-calendar-check"
    }
    if (type === "POST_COMMENTED" || type === "COMMENT_REPLIED") {
        toast.addEventListener("click",()=>{
            window.location.href="/freeboard/detail?no="+related_id
        })
        badgeEl.classList.add("text-primary", "bg-primary-subtle")
        iconEl.className = "fa-solid fa-bell"
    }


    const titleEl = document.createElement("strong")
    titleEl.textContent = title

    const contentEl = document.createElement("p")
    contentEl.textContent = content

    badgeEl.appendChild(iconEl)
    titleSectionEl.append(badgeEl, titleEl)

    toast.appendChild(titleSectionEl)
    toast.appendChild(contentEl)

    document.body.appendChild(toast);

    setTimeout(() => {
        //페이드 적용안됨 오류 [수정필요]
        toast.classList.add("fade-out")
        setTimeout(() => toast.remove(), 300)
    }, 6000)
}
