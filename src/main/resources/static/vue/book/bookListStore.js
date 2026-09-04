const { defineStore } = Pinia;

const useBookListStore = defineStore('bookList', {
    state: () => ({
        list: [],
        count: 0,
        curpage: 1,
        totalpage: 0,
        startPage: 0,
        endPage: 0,
        range: [],
        keyword: '',
        isSearchMode: false,

        categories: [
            '전체',
            '오픈소스 & 웹',
            '데이터 사이언스',
            'IT Leaders',
            '임베디드 & 모바일',
            '프로그래밍 & 프랙티스',
            '게임 개발',
            '해킹 & 보안',
            '생성형 AI 프로그래밍',
            'UX 디자인',
            '위키미디어'
        ],
        selectedCategory: '전체',
        sortOption: '출간일 순'
    }),

    actions: {
        async bookListData() {
            try {
				// 일반 모드
				this.isSearchMode = false;
                const response = await axios.get('/book/list_vue', {
                    params: {
                        page: this.curpage,
                        category: this.selectedCategory,
                        sort: this.sortOption
                    }
                });
                this.list = response.data.list;
                this.count = response.data.count;
                this.curpage = response.data.curpage;
                this.totalpage = response.data.totalpage;
                this.startPage = response.data.startPage;
                this.endPage = response.data.endPage;

                // range 배열 생성
                this.range = [];
                for (let i = this.startPage;i <= this.endPage;i++) {
                    this.range.push(i);
                }

            } catch (error) {
                console.error(error);
            }
        },
        async findBook(page = 1) {
            if (!this.keyword.trim()) {
                alert('검색어를 입력해주세요.');
                return;
            }

            try {
                this.selectedCategory = '전체';
                // 백엔드의 검색 API 호출 
                const response = await axios.get('/book/api/find', {
                    params: {
                        keyword: this.keyword,
                        page: page,
                        category: this.selectedCategory,
                        sort: this.sortOption
                    }
                });
				// 검색 모드 
                this.isSearchMode = true;

                this.list = response.data.list;
                this.count = response.data.count;
                this.curpage = response.data.curpage;
                this.totalpage = response.data.totalpage;
                this.startPage = response.data.startPage;
                this.endPage = response.data.endPage;
                this.range = response.data.range;


            } catch (error) {
                console.error('검색 오류:', error);
                alert('검색 중 오류가 발생했습니다.');
            }
        },

        move(page) {
            if (this.isSearchMode) {
                this.findBook(page);
            } else {
                this.curpage = page;
                this.bookListData();
            }
        },

        setCategory(cat) {
            this.selectedCategory = cat;
            this.curpage = 1;
            if (this.isSearchMode) {
                this.findBook(1);
            } else {
                this.bookListData();
            }
        },

        setSortOption(opt) {
            this.sortOption = opt;
            this.curpage = 1;
            if (this.isSearchMode) {
                this.findBook(1);
            } else {
                this.bookListData();
            }
        }
    }
});