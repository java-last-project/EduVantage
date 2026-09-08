(function (){
    //[WHY] 헤더의 정적 아이콘을 Vue 앱으로 다시 마운트하면 innerHTML이 초기화됐다가 재렌더링되어
    //깜빡임/사라짐이 발생함 -> 별도 앱 없이 pinia store 구독만으로 뱃지 표시 여부만 토글
    const badge = document.getElementById("notification-badge")

    if(badge){
        // notificationPinia를 명시적으로 지정해 다른 페이지의 pinia와 충돌하지 않도록 함
        const store = useNotificationStore(notificationPinia)

        const syncBadge = () => {
            badge.style.display = store.hasUnread ? "" : "none"
        }

        syncBadge()
        store.$subscribe(syncBadge)
    }
})()
