/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    reply:{},
	qnaNo:0,
	no:0
})
const useQnaReplyStore=defineStore('qnaReply_store',{
    state:initialState,
    actions:{
        async qnaReplyData(params){
			try{
	            const res=await api.get('/qna/reply', {
					params:{
		                qnaNo:this.qnaNo
					}
	            })
	            console.log(res.data)
	            this.reply=res.data.reply
			}catch(error){
			console.error(error)
			}
		},
		async insertReply(insertData){
			try{
				await api.post('/qna/reply/insert',insertData)
				await this.qnaReplyData()
			}catch(error){
				console.error(error)
			}
		},
		async updateReply(updateData){
			try{
				await api.post('/qna/reply/update',updateData)
					await this.qnaReplyData()
			}catch(error){
				console.error(error)
			}
		},
		async deleteReply(){
			try{
				await api.get('/qna/reply/delete',{
					params:{
						no:this.no,
						qnaNo:this.qnaNo
					}
				})
				await this.qnaReplyData()
			}catch(error){
				console.error(error)
			}
		}
	}
})