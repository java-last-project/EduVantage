const { defineStore } = Pinia;

const useBookDetailStore = defineStore('bookDetail', {
    state: () => ({
        vo: {}, 
        activeTab: 'intro' // intro(도서소개), author(저자소개), toc(목차)
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
		// 추가: 좋아요 상태 조회
		        async fetchLikeStatus(no) {
		            try {
		                const response = await axios.get('/book/api/like/status', {
		                    params: { book_no: no }
		                });
		                this.isLiked = response.data.isLiked;
		                this.likeCount = response.data.likeCount;
		            } catch (error) {
		                console.error(error);
		            }
		        },

		        // 추가: 좋아요 토글
		        async toggleLike() {
		            try {
		                const response = await axios.post('/book/api/like/toggle', null, {
		                    params: { book_no: this.vo.no }
		                });

		                if (response.data.error) {
		                    alert(response.data.error); // 로그인 안 한 경우
		                    return;
		                }

		                this.isLiked = response.data.isLiked;
		                this.likeCount = response.data.likeCount;
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