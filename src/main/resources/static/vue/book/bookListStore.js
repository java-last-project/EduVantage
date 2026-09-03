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
                for(let i = this.startPage; i <= this.endPage; i++) {
                    this.range.push(i);
                }
                
            } catch (error) {
                console.error(error);
            }
        },
        
        move(page) {
            this.curpage = page;
            this.bookListData();
        },
        
        setCategory(cat) {
            this.selectedCategory = cat;
            this.curpage = 1; 
            this.bookListData();
        },
        
        setSortOption(opt) {
            this.sortOption = opt;
            this.curpage = 1; 
            this.bookListData();
        }
    }
});