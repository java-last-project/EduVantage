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
                // 저장된 currentTime부터 이어보기
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
            // UI progress 갱신 주기
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
            // UI 갱신과 DB 저장 주기 분리
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
                // 영상 전환 전 마지막 재생 위치 저장
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

        const handlePageHide=()=>{
            if(!player || !selectedVideo.value) return
            const currentTime=Math.floor(player.getCurrentTime())
            const duration=Math.floor(player.getDuration())
            if(duration<=0) return

            const progress=Math.floor(currentTime/duration*100)

            // pagehide 시 keepalive로 마지막 진도 저장
            fetch('/api/enrollment/progress',{
                method:'POST',
                headers:{
                    'Content-Type':'application/json'
                },
                body:JSON.stringify({
                    enrollment_no:initialEnrollmentNo,
                    video_no:selectedVideo.value.no,
                    currentTime,
                    duration,
                    progress
                }),
                keepalive:true
            })
        }

        onMounted(async()=>{
            loadVideos()
            await videoStore.loadProgress(initialEnrollmentNo)
            window.addEventListener('pagehide',handlePageHide)
            window.onYouTubeIframeAPIReady=()=>{
                createPlayer()
            }
            if(window.YT && window.YT.Player){
                createPlayer()
            }
        })

        onBeforeUnmount(()=>{
            window.removeEventListener("pagehide",handlePageHide)
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