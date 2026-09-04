/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    list:[],
    board_no:0,
    member_id:0,
    parentNo:0,
    curpage:1,
    totalpage:0,
    startPage:0,
    endPage:0,
    count:0
})
const useFreeCommentStore=defineStore('freecomment_store',{
    state:initialState,
    getters:{
        range:(state)=>{
            const arr=[]
            for(let i=state.startPage;i<=state.endPage;i++){
                arr.push(i)
            }
            return arr
        }
    },
    actions:{
        async freeCommentListData(params){
            try{
                const res=await api.get('/freeboard/comment_vue', {
                    params:{
                        page: this.curpage,
                        board_no: this.board_no
                    }
                })
                // console.log(res.data)
                this.list=res.data.list
                this.curpage=res.data.curpage
                this.totalpage=res.data.totalpage
                this.startPage=res.data.startPage
                this.endPage=res.data.endPage
                this.count=res.data.count
            }catch(error){
                console.error(error)
            }
        },
        async move(page){
            this.curpage=page
            await this.freeCommentListData()
        },
        async insertComment(insertData){
            try{
                await api.post('/freeboard/comment_insert_vue',insertData)
                this.curpage=1
                await this.freeCommentListData()
            }catch(error){
                console.error(error)
            }
        },
        async updateComment(updateData){
            try{
                await api.post('/freeboard/comment_update_vue',null,{
                    params:updateData
                })
                await this.freeCommentListData()
            }catch(error){
                console.error(error)
            }
        },
        async deleteComment(no){
            try{
                await api.get('/freeboard/comment_delete_vue',{
                    params:{
                        no:no
                    }
                })
                await this.freeCommentListData()
            }catch(error){
                console.error(error)
            }
        }
    }
})
