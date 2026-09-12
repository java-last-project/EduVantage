const {createApp,onMounted,onBeforeUnmount}=Vue
const {createPinia,storeToRefs}=Pinia
const pinia=createPinia()
const videoApp=createApp({
    setup(){
        const videoStore=useVideoStore()

        const {
            videos,
            selectedVideo,
            isLoading,
            errorMessage,
            completedCount,
            overallProgress
        }=storeToRefs(videoStore)

        const courseTitle=initialCourseTitle

        let player=null
        let progressTimer=null
        let saveTimer=null

        const loadVideos=()=>{
            videoStore.initVideos(initialYoutubeVideos)
        }
        const createPlayer=()=>{
            if(!selectedVideo.value) return
            player=new YT.Player('youtube-player',{
                videoId:selectedVideo.value.videoId,
                events:{
                    onReady:onPlayerReady,
                    onStateChange:onPlayerStateChange
                }
            })
        }
        const onPlayerReady=()=>{
            if(!selectedVideo.value) return
            const currentTime=selectedVideo.value.currentTime || 0
            if(currentTime>0){
                player.seekTo(currentTime,true)
            }
        }
        const onPlayerStateChange=(e)=>{
            if(e.data===YT.PlayerState.PLAYING){
                startProgressTimer()
                startSaveTimer()
            }
            if(e.data===YT.PlayerState.PAUSED || e.data===YT.PlayerState.ENDED){
                updateProgress()
                saveProgress()

                stopProgressTimer()
                stopSaveTimer()
            }
        }
        const startProgressTimer=()=>{
            if(progressTimer) return
            progressTimer=setInterval(()=>{
                updateProgress()
            },1000)
        }
        const stopProgressTimer=()=>{
            if(!progressTimer) return
            clearInterval(progressTimer)
            progressTimer=null
        }
        const updateProgress=()=>{
            if(!player || !selectedVideo.value) return
            const currentTime=player.getCurrentTime()
            const duration=player.getDuration()

            if(duration<=0) return
            const progress=Math.floor(currentTime/duration*100)

            videoStore.updateProgress(
                selectedVideo.value.videoId,
                progress
            )
        }

        const saveProgress=async()=>{
            if(!player || !selectedVideo.value) return

            const currentTime=Math.floor(player.getCurrentTime())
            const duration=Math.floor(player.getDuration())
            if(duration<=0) return
            const progress=Math.floor(currentTime/duration*100)

            videoStore.updateProgress(
                selectedVideo.value.videoId,
                progress
            )

            await videoStore.saveProgress({
                enrollment_no:initialEnrollmentNo,
                video_no:selectedVideo.value.no,
                currentTime,
                duration,
                progress
            })
        }
        const startSaveTimer=()=>{
            if(saveTimer) return
            saveTimer=setInterval(()=>{
                saveProgress()
            },30000)
        }
        const stopSaveTimer=()=>{
            if(!saveTimer) return
            clearInterval(saveTimer)
            saveTimer=null
        }

        const selectVideo=async(videoId)=>{
            if(selectedVideo.value){
                await saveProgress()
            }
            videoStore.selectVideo(videoId)
            if(player && selectedVideo.value){
                player.loadVideoById({
                    videoId: selectedVideo.value.videoId,
                    startSecond: selectedVideo.value.currentTime || 0
                })
            }
        }

        onMounted(async()=>{
            loadVideos()
            await videoStore.loadProgress(initialEnrollmentNo)
            window.onYouTubeIframeAPIReady=()=>{
                createPlayer()
            }
            if(window.YT && window.YT.Player){
                createPlayer()
            }
        })

        onBeforeUnmount(()=>{
            saveProgress()
            stopProgressTimer()
            stopSaveTimer()
            if(player){
                player.destroy()
            }
        })

        return{
            videos,
            selectedVideo,
            isLoading,
            errorMessage,
            completedCount,
            overallProgress,
            courseTitle,
            loadVideos,
            selectVideo
        }
    }
})
videoApp.use(pinia)
videoApp.mount('#video-app')