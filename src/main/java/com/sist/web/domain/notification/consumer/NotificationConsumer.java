package com.sist.web.domain.notification.consumer;

import com.sist.web.domain.member.mapper.MemberMapper;
import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.notification.entity.NotificationTopics;
import com.sist.web.domain.notification.entity.NotificationType;
import com.sist.web.domain.notification.entity.Notifications;
import com.sist.web.domain.notification.repository.EmitterRepository;
import com.sist.web.domain.notification.repository.NotificationRepository;
import com.sist.web.domain.notification.vo.NotificationEventVO;
import com.sist.web.domain.notification.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * [WHAT] 알림 컨슈머: 토픽,데이터가 오면, 해당 핸들러 함수 실행
 *              -> 알림 title/content 조립
 *              -> DB 저장
 *              -> 유저 접속 여부 확인(emitter)
 *              -> 점속중: 실시간 push(토스트 알림)
 *                 미접속: 로그인 후 조회 가능
 */
@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final MemberMapper memberMapper;

    //groupid를 명시해서 처리하는 컨슈머 인스턴스를 나눠야하는 이유
    //강의 수료 리스너
    @KafkaListener(topics=NotificationTopics.COURSE_COMPLETED, groupId = "notification-group")
    public void handleCourseCompleted(NotificationEventVO event) {
        String title = "수료를 축하합니다!";
        String content = event.getTarget() + " 과정을 성공적으로 마치셨습니다.";

        //db저장
        Notifications notification = Notifications.builder()
                .memberId(event.getMemberId())
                .type(NotificationType.COURSE_COMPLETED)
                .title(title)
                .content(content)
                .relatedId(event.getTargetNo())
                .eventKey(event.getEventKey())
                .build();
        notificationRepository.save(notification);
        int no = notification.getNo();

        //유저 접속 여부 확인(emitter)
        emitterRepository.findByMemberId(event.getMemberId())
                //온라인
                .ifPresent(emitter -> {
                    try{
                       emitter.send(SseEmitter.event()
                               .data(Map.of("no", no,"type",NotificationType.COURSE_COMPLETED.toString(),"title",title,"content",content)));
                    }catch(IOException e){
                        emitterRepository.deleteByMemberId(event.getMemberId());
                    }
                });
    }

    //댓글에 답변 알림 리스너
    @KafkaListener(topics= NotificationTopics.COMMENT_REPLIED, groupId = "notification-group")
    public void handleCommunityNews(NotificationEventVO event){
        String title = "댓글에 답변이 달렸어요!";
        String content = "눌러서 바로 게시글로 이동해보세요.";

        //db저장
        Notifications notification = Notifications.builder()
                .memberId(event.getMemberId())
                .type(NotificationType.COMMENT_REPLIED)
                .title(title)
                .content(content)
                .relatedId(event.getTargetNo())
                .eventKey(event.getEventKey())
                .build();
        notificationRepository.save(notification);
        int no = notification.getNo();

        //유저 접속 여부 확인(emitter)
        emitterRepository.findByMemberId(event.getMemberId())
                //온라인
                .ifPresent(emitter -> {
                    try{
                        emitter.send(SseEmitter.event()
                                .data(Map.of("no", no,"type",NotificationType.COMMENT_REPLIED.toString(),"title",title,"content",content)));
                    }catch(IOException e){
                        emitterRepository.deleteByMemberId(event.getMemberId());
                    }
                });

    }

    //내 글에 댓글 알림 리스너
    @KafkaListener(topics = NotificationTopics.POST_COMMENTED, groupId = "notification-group")
    public void handlePostCommented(NotificationEventVO event){
        String title = "["+event.getTarget()+"]에 답변이 달렸어요!";
        String content = "눌러서 바로 게시글로 이동해보세요.";

        //db저장
        Notifications notification = Notifications.builder()
                .memberId(event.getMemberId())
                .type(NotificationType.POST_COMMENTED)
                .title(title)
                .content(content)
                .relatedId(event.getTargetNo())
                .eventKey(event.getEventKey())
                .build();
        notificationRepository.save(notification);
        int no = notification.getNo();
        int related_id = notification.getRelatedId();

        //유저 접속 여부 확인(emitter)
        emitterRepository.findByMemberId(event.getMemberId())
                //온라인
                .ifPresent(emitter -> {
                    try{
                        emitter.send(SseEmitter.event()
                                .data(Map.of("no", no,"type",NotificationType.POST_COMMENTED.toString(),"title",title,"content",content,"related_id", related_id)));
                    }catch(IOException e){
                        emitterRepository.deleteByMemberId(event.getMemberId());
                    }
                });
    }

    //대댓글 알림 리스너
    @KafkaListener(topics = NotificationTopics.COMMENT_REPLIED, groupId = "notification-group")
    public void handleCommentReplied(NotificationEventVO event){
        String title = "내 댓글에 답변이 달렸어요!";
        String content = "눌러서 바로 ["+event.getTarget()+"] 게시글로 이동해보세요.";

        //db저장
        Notifications notification = Notifications.builder()
                .memberId(event.getMemberId())
                .type(NotificationType.COMMENT_REPLIED)
                .title(title)
                .content(content)
                .relatedId(event.getTargetNo())
                .eventKey(event.getEventKey())
                .build();
        notificationRepository.save(notification);
        int no = notification.getNo();
        int related_id = notification.getRelatedId();

        //유저 접속 여부 확인(emitter)
        emitterRepository.findByMemberId(event.getMemberId())
                //온라인
                .ifPresent(emitter -> {
                    try{
                        emitter.send(SseEmitter.event()
                                .data(Map.of("no", no,"type",NotificationType.COMMENT_REPLIED.toString(),"title",title,"content",content,"related_id", related_id)));
                    }catch(IOException e){
                        emitterRepository.deleteByMemberId(event.getMemberId());
                    }
                });
    }

    //공지 전역 알림 리스너
    @KafkaListener(topics = NotificationTopics.NOTICE_UPLOAD, groupId = "notification-group")
    public void handleNoticeUpload(NotificationEventVO event) {
        String title = "새로운 공지가 등록됐어요!";
        String content = "눌러서 바로 [" + event.getTarget() + "] 게시글로 이동해보세요.";

        //관리자 제외 모든 유저에게 알림 전송
        List<Integer> memberIds = memberMapper.getAllMemberIdsExcludeAdmin();
        String eventKey=null;
        for (int memberId : memberIds) {
            eventKey = UUID.randomUUID().toString();
            //db저장
            Notifications notification = Notifications.builder()
                    .memberId(memberId)
                    .type(NotificationType.NOTICE_UPLOAD)
                    .title(title)
                    .content(content)
                    .relatedId(event.getTargetNo())
                    .eventKey(eventKey)
                    .build();
            notificationRepository.save(notification);
            int no = notification.getNo();
            int related_id = notification.getRelatedId();

            //유저 접속 여부 확인(emitter)
            emitterRepository.findByMemberId(memberId)
                    //온라인
                    .ifPresent(emitter -> {
                        try {
                            emitter.send(SseEmitter.event()
                                    .data(Map.of("no", no, "type", NotificationType.NOTICE_UPLOAD.toString(), "title", title, "content", content, "related_id", related_id)));
                        } catch (IOException e) {
                            emitterRepository.deleteByMemberId(memberId);
                        }
                    });
        }
    }
}
