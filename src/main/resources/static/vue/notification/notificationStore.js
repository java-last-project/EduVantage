const { defineStore } = Pinia;

//js -> pinia로 바꾼 이유: 헤더의 알림 아이콘과 알림모달이 fragment로 분리되어있어서 통합관리가 어려움
//하나를 바꾸면 알림뱃지는 수동으로 변경해줘야함. -> pinia 도입으로 전역관리

const useNotificationStore = defineStore('notification', {
    //[state]: 기존 ref()로 선언한 것, 전달해야하는 데이터 묶음
    state: ()=> ({
        nnList: []
    }),
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
                const res = await api.patch("/notification/read-all",{nos})
                //읽은 알림 회색처리 or 재조회해서 읽은알림/안읽은알림 UI 분기처리
            }catch(err){
                console.error(err)
            }
        },
        //sse로 새알림이 온 경우
        addNotification(data){
            this.nnList.unshift(data)
        }
    }
})