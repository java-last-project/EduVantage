const { createApp, onMounted, computed } = Vue;
const { createPinia } = Pinia;

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

notificationApp.use(createPinia())
notificationApp.mount("#notificationModal")