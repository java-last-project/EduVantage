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
        showToast(data.title, data.content)
    }

    //sse 연결끊겼을 경우
    es.onerror = () => {
        console.log("sse 연결 끊김")
    }
}

function showToast(title, content){
    const toast = document.createElement("div")
    toast.className = "sse-toast"

    const titleEl = document.createElement("strong")
    titleEl.textContent = title

    const contentEl = document.createElement("p")
    contentEl.textContent = content

    toast.appendChild(titleEl)
    toast.appendChild(contentEl)

    document.body.appendChild(toast);

    setTimeout(()=>{
        toast.classList.add("fade-out")
        toast.remove()
    }, 6000)
}