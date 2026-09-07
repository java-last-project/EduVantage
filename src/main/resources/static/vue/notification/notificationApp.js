(function(){
    const { createApp, onMounted, computed } = Vue;
    const { createPinia } = Pinia;

    window.notificationPinia = createPinia()

    const notificationApp = createApp({
        setup(){
            const store = useNotificationStore()
            onMounted(()=>{
                store.fetchNotifications()
            })

            return {
                nnList: computed(()=>store.nnList),
                markAllRead: store.markAllRead
            }
        }
    })

    notificationApp.use(window.notificationPinia)
    notificationApp.mount("#notificationModal")
})()