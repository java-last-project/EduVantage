const useBookCommentStore = Pinia.defineStore('bookComment', {
    state: () => ({
        list: [],        // 댓글 배열
        count: 0,        // 총 댓글 개수
        fno: 0,          // 도서 번호
        curpage: 1,      // 현재 페이지
        totalpage: 0     // 전체 페이지 수
    }),

    actions: {
        // 1. 댓글 목록 조회
        async commentListData() {
            try {
                const res = await axios.get('/comment/list_vue', {
                    params: {
                        fno: this.fno,
                        page: this.curpage
                    }
                });
                this.list = res.data.rList;
                this.count = res.data.count;
                this.curpage = res.data.curpage;
                this.totalpage = res.data.totalpage;
            } catch (error) {
                console.error("댓글 목록 로드 실패:", error);
            }
        },

        // 2. 일반 새 댓글 및 대댓글 등록
        async insertComment(payload) {
            try {
                const res = await axios.post('/comment/insert_vue', {
                    // 백엔드 BookCommentVO의 멤버 변수명과 일치시킴
                    book_no: payload.fno,
                    member_id: payload.member_id,
                    msg: payload.msg,
                    root: payload.root || 0   // 기존 parent_no 대신 root 사용 (일반 댓글은 0)
                });

                this.list = res.data.rList;
                this.count = res.data.count;
                this.curpage = res.data.curpage;
                this.totalpage = res.data.totalpage;
            } catch (error) {
                console.error("댓글 등록 실패:", error);
            }
        },

        // 3. 댓글 수정
        async updateComment(payload) {
            try {
                const res = await axios.put('/comment/update_vue', {
                    no: payload.no,
                    msg: payload.msg,
                    book_no: this.fno // 갱신된 리스트를 받기 위해 도서 번호 전달
                });

                this.list = res.data.rList;
                this.count = res.data.count;
                this.curpage = res.data.curpage;
                this.totalpage = res.data.totalpage;
            } catch (error) {
                console.error("댓글 수정 실패:", error);
            }
        },

        // 4. 댓글 삭제
        async deleteComment(no) {
            try {
                const res = await axios.delete('/comment/delete_vue', {
                    params: {
                        no: no,
                        fno: this.fno
                    }
                });

                this.list = res.data.rList;
                this.count = res.data.count;
                this.curpage = res.data.curpage;
                this.totalpage = res.data.totalpage;
            } catch (error) {
                console.error("댓글 삭제 실패:", error);
            }
        }
    }
});