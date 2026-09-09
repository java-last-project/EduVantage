(function(){
    const { createApp, onMounted, computed } = Vue;

    // notificationPinia는 notificationStore.js에서 전역으로 생성한 단일 인스턴스를 재사용함
    const notificationApp = createApp({
        setup(){
            const store = useNotificationStore(notificationPinia)
            onMounted(()=>{
                store.fetchNotifications()
            })

            return {
                nnList: computed(()=>store.nnList),
                markAllRead: store.markAllRead,
                markRead: store.markRead,
                move: store.move,
                handleClick: store.handleClick
            }
        }
    })

    notificationApp.use(notificationPinia)
    notificationApp.mount("#notificationModal")
})()