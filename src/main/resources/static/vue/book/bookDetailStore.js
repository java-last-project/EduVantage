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
                const response = await axios.get('/book/like/status', {
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
                const response = await axios.post('/book/like/toggle', null, {
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
		async addToCart() {
		    if (typeof SESSION_ID === 'undefined' || SESSION_ID === null || SESSION_ID === 0) {
		        alert('로그인이 필요한 서비스입니다.');
		        return;
		    }

		    try {
		        const response = await axios.post('/cart/add', {
		            book_no: this.vo.no,
		            member_id: SESSION_ID, 
		            quantity: 1 
		        });

		        if (response.data.status === 'success') {
		            if (confirm('장바구니에 추가되었습니다. 장바구니로 이동하시겠습니까?')) {
		                location.href = '/mypage/carts?tab=book';
		            }
		        } else {
		            alert('장바구니 담기에 실패했습니다.');
		        }
		    } catch (error) {
		        console.error(error);
		        alert('서버 오류가 발생했습니다.');
		    }
		},
        goToOrder() {
            location.href = '/mypage/orders?tab=book';
        }
    }
});