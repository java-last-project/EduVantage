(function (){
    const { createApp, computed } = Vue;
    const { createPinia } = Pinia;

    const badgeWrapper = document.getElementById("notification-badge-wrapper")

    if(badgeWrapper){
        const badgeApp = createApp({
            setup(){
                const store = useNotificationStore()
                return {
                    hasUnread: computed(()=> store.hasUnread)
                }
            }
        })
        badgeApp.use(window.notificationPinia)
        badgeApp.mount("#notification-badge-wrapper")
    }
})()