(function (){
    //[WHY] 헤더의 정적 아이콘을 Vue 앱으로 다시 마운트하면 innerHTML이 초기화됐다가 재렌더링되어
    //깜빡임/사라짐이 발생함 -> 별도 앱 없이 pinia store 구독만으로 뱃지 표시 여부만 토글
    const badge = document.getElementById("notification-badge")

    if(badge){
        const store = useNotificationStore()

        const syncBadge = () => {
            badge.style.display = store.hasUnread ? "" : "none"
        }

        syncBadge()
        store.$subscribe(syncBadge)
    }
})()
