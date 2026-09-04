const { defineStore } = Pinia;

const useBookDetailStore = defineStore('bookDetail', {
    state: () => ({
        vo: {}, // 도서 상세 정보 (BookVO)
        activeTab: 'intro' // 'intro'(도서소개), 'author'(저자소개), 'toc'(목차)
    }),
    
    actions: {
        async fetchBookDetail(no) {
            try {
                // BookRestController에 추가할 상세조회 API 경로
                const response = await axios.get('/book/detail_vue', {
                    params: { no: no }
                });
                this.vo = response.data;
            } catch (error) {
                console.error(error);
            }
        },
        setTab(tabName) {
            this.activeTab = tabName;
        },
        goToCart() {
            location.href = '/book/cart';
        },
        goToOrder() {
            location.href = '/book/order';
        }
    }
});