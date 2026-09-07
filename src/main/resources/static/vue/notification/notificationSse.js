//header에 알림모듈이 있는 상태(로그인된 상태)에만 연결
const badge = document.getElementById("notification-badge")

if(badge){
    //[WHAT] EventSource: 브라우저 내장객체, 서버->클라이언트 데이터 통로 생성
    //[변경예정] 세션기반에 따라서 쿠키에 JSESSIONID를 실어보내도록 설정 (요청 구분)
    const es = new EventSource("/sse", {withCredentials: true})

    // 서버에서 데이터를 보낼 때(emitter.send())마다 실행
    es.onmessage = (e) => {
        const data = JSON.parse(e.data)
        const store = useNotificationStore()
        store.addNotification(data)

        showToast(data.type, data.title, data.content)
    }

    es.onerror = () => {
        console.log("sse 연결 끊김")
    }
}

function showToast(type, title, content) {
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

