const { defineStore } = Pinia;

const useCheckoutStore = defineStore('checkoutStore', {
    state: () => ({
        book: {},
        member: {},
        quantity: 1,
        useDefaultAddress: true,
        sessionId: 0
    }),
    actions: {
        // 도서 정보 조회
        async fetchBookInfo(no) {
            try {
                const res = await axios.get('/book/detail_vue', { params: { no: no } });
                this.book = res.data;
            } catch (error) {
                console.error("도서 로드 실패", error);
            }
        },
        // 회원(주소) 정보 조회
        async fetchMemberInfo() {
            try {
                const res = await axios.get('/member/info_vue');
                this.member = res.data;
            } catch (error) {
                console.error("회원 로드 실패", error);
            }
        },
        // 배송지 체크박스 
        handleAddressToggle() {
            if (this.useDefaultAddress && !this.member.name) {
                this.fetchMemberInfo();
            }
        },
        // 결제 요청
        requestPay() {
            if (!this.useDefaultAddress) {
                alert("배송지를 선택해주세요.");
                return;
            }

            const IMP = window.IMP;
            IMP.init('imp16373064');

            IMP.request_pay({
                pg: "html5_inicis",
                pay_method: "card",
                merchant_uid: "ORDER_" + new Date().getTime(),
                name: this.book.title,
                amount: this.book.price * this.quantity,
                buyer_email: this.member.email,
                buyer_name: this.member.name,
                buyer_tel: this.member.phone,
                buyer_addr: `${this.member.addr1} ${this.member.addr2}`
            }, (rsp) => {
                // 성공/취소 모두 DB에 강제 저장
                this.saveOrderToDB();
            });
        },
        // DB 저장
        async saveOrderToDB() {
            try {
                const orderData = {
                    member_id: this.sessionId,
                    total_price: this.book.price * this.quantity,
                    detailList: [{
                        book_no: this.book.no,
                        quantity: this.quantity,
                        price: this.book.price
                    }]
                };

                const res = await axios.post('/order/save', orderData);
                if (res.data.status === 'success') {
                    alert("결제가 완료되었습니다!");
                    location.href = '/mypage/orders?tab=book';
                }
            } catch (error) {
                console.error("저장 에러", error);
            }
        }
    }
});