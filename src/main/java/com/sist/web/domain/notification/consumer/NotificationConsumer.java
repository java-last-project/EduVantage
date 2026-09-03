package com.sist.web.domain.notification.consumer;

import com.sist.web.domain.notification.entity.NotificationType;
import com.sist.web.domain.notification.entity.Notifications;
import com.sist.web.domain.notification.repository.EmitterRepository;
import com.sist.web.domain.notification.repository.NotificationRepository;
import com.sist.web.domain.notification.vo.NotificationEventVO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;


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

    //groupid를 명시해서 처리하는 컨슈머 인스턴스를 나눠야하는 이유
    //강의 수료 리스너
    @KafkaListener(topics="course-completed", groupId = "notification-group")
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

        //유저 접속 여부 확인(emitter)
        emitterRepository.findByMemberId(event.getMemberId())
                //온라인
                .ifPresent(emitter -> {
                    try{
                       emitter.send(SseEmitter.event()
                               .data(Map.of("type",NotificationType.COURSE_COMPLETED.toString(),"title",title,"content",content)));
                    }catch(IOException e){
                        emitterRepository.deleteByMemberId(event.getMemberId());
                    }
                });
    }
}
