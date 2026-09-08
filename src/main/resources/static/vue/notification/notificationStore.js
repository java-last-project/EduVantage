//[WHY] 최상위에서 const { defineStore } = Pinia 를 선언하면, 이 파일과 함께 로드되는
//각 페이지 전용 store 파일(examStore.js, courseStore.js 등)도 동일하게 최상위에서
//defineStore를 선언하기 때문에 "Identifier 'defineStore' has already been declared" 충돌이 남
//(일반 <script> 태그들은 같은 문서 안에서 최상위 const/let 스코프를 공유함).
//IIFE로 감싸서 내부 변수가 전역으로 새지 않게 하고, 다른 스크립트에서 써야 하는
//notificationPinia/useNotificationStore만 window에 명시적으로 노출함.
(function(){
    const { defineStore } = Pinia;

    //js -> pinia로 바꾼 이유: 헤더의 알림 아이콘과 알림모달이 fragment로 분리되어있어서 통합관리가 어려움
    //하나를 바꾸면 알림뱃지는 수동으로 변경해줘야함. -> pinia 도입으로 전역관리

    //[WHY] 페이지마다 createApp().use(createPinia())로 별도 pinia를 만들면
    //pinia의 "활성(active) 인스턴스"가 그때그때 덮어써짐 -> useNotificationStore()를
    //인자 없이 호출하는 곳(sse, badge)이 엉뚱한 pinia에 바인딩되는 충돌이 발생했음.
    //알림 기능 전용 pinia를 여기서 한 번만 만들어 전역으로 공유하고, 모든 알림 스크립트가
    //이 인스턴스를 명시적으로 지정해서 store를 가져오도록 통일함.
    const notificationPinia = Pinia.createPinia();

    const useNotificationStore = defineStore('notification', {
        //[state]: 기존 ref()로 선언한 것, 전달해야하는 데이터 묶음
        state: ()=> ({
            nnList: []
        }),
        //[getters]: ...
        getters: {
            hasUnread: (state)=>state.nnList.some(n=>n.read === false)
        },
        //[actions]: 기존 함수들
        actions: {
            async fetchNotifications() {
                try {
                    const res = await api.get("/notification")
                    this.nnList = res.data
                    console.log(res)
                } catch (error) {
                    console.error(error)
                }
            },

            async markAllRead() {
                if(this.nnList.length === 0) return
                const nos = this.nnList.map(n => n.no)
                try {
                    await api.patch("/notification/read-all",{nos})
                    //읽은 알림 회색처리 or 재조회해서 읽은알림/안읽은알림 UI 분기처리
                    await this.fetchNotifications()
                }catch(err){
                    console.error(err)
                }
            },
            //sse로 새알림이 온 경우
            addNotification(data){
                console.log(data)
                this.nnList.unshift(data)
            },
            //관련 url 이동
            move(notification){
                if(notification.related_id != null){
                    if(notification.type === "POST_COMMENTED"){
                        console.log(notification)
                        console.log(notification.related_id)
                        window.location.href="/freeboard/detail?no="+notification.related_id
                    }
                }
            }
        }
    })

    window.notificationPinia = notificationPinia;
    window.useNotificationStore = useNotificationStore;
})();