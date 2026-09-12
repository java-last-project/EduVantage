/** @type {import('pinia')}*/
const {defineStore}=Pinia
const initialState=()=>({
    videos:[],
    selectedVideo:null,
    isLoading:false,
    errorMessage:''
})
const useVideoStore=defineStore('video_store',{
    state:initialState,
    getters:{
        completedCount:(state)=>{
            return state.videos.filter(video=>video.completed).length
        },
        // 전체 진도율
        overallProgress: (state)=>{
            if(state.videos.length===0) return 0
            const completedCount=state.videos.filter(video=>video.completed).length
            return Math.round(completedCount/state.videos.length*100)
        }
    },
    actions:{
        initVideos(videoList){
            this.videos=(videoList || []).map(video=>({
                no:video.no,
                videoId:video.videoId,
                title:video.title,
                thumbnail:video.thumbnail,
                progress:0,
                completed:false,
                currentTime:0
            }))
            if(this.videos.length>0){
                this.selectedVideo=this.videos[0]
            }
        },
        async saveProgress(progressData){
            try{
                await api.post('/api/enrollment/progress',progressData)
            }catch (error){
                console.error(error)
            }
        },
        selectVideo(videoId){
            const video=this.videos.find(video=>video.videoId===videoId)
            if(!video) return
            this.selectedVideo=video
        },
        updateProgress(videoId,progress){
            const video=this.videos.find(video=>video.videoId===videoId)
            if(!video) return
            video.progress=progress
            if(progress>=90){
                video.completed=true
            }
        },
        async loadProgress(enrollmentNo){
            try{
                const res=await api.get(`/api/enrollment/progress/${enrollmentNo}`)
                res.data.forEach(progressData=>{
                    const video=this.videos.find(
                        video=>video.no===progressData.video_no
                    )
                    if(!video) return
                    video.progress=progressData.progress
                    video.completed=progressData.completed==='Y'
                    video.currentTime=progressData.currentTime
                })
            }catch(error){
                console.error(error)
            }
        }
    }
})