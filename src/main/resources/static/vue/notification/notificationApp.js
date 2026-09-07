(function(){
    const { createApp, onMounted, computed } = Vue;
    const { createPinia } = Pinia;

    const notificationPinia = createPinia()

    const notificationApp = createApp({
        setup(){
            const store = useNotificationStore(notificationPinia)
            onMounted(()=>{
                store.fetchNotifications()
            })

            return {
                nnList: computed(()=>store.nnList),
                markAllRead: store.markAllRead
            }
        }
    })

    notificationApp.use(notificationPinia)
    notificationApp.mount("#notificationModal")
})()