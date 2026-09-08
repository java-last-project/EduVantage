package com.sist.web.domain.notification.producer;

import com.sist.web.domain.notification.entity.NotificationTopics;
import com.sist.web.domain.notification.vo.NotificationEventVO;
import lombok.RequiredArgsConstructor;
//카프카 템플릿: 메시지를 발행할 때 쓰는 스프링 제공 도구
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationEventVO> kafkaTemplate;

    /**
     * [WHAT] 수료 이벤트 발생 시, 카프카 토픽에 넣는 역할
     * [WHY] 프로듀서: 전달 / 컨슈머: 로직 -> 수료판정 로직이 이벤트 발행만 호출하면 되므로, 결합력 감소
     */
    public void publishCompletion(int memberId,  int courseNo, String courseTitle) {
        kafkaTemplate.send(NotificationTopics.COURSE_COMPLETED, new NotificationEventVO(
                memberId, courseNo, courseTitle, UUID.randomUUID().toString()
        ));
    }

    /**
     * [WHAT] 대댓글 알림, 카프카 토픽에 넣는 역할
     * [WHY] 프로듀서: 전달 / 컨슈머: 로직 -> 결합력 감소
     */
    public void publishReplied(int memberId,  int replyNo) {
        kafkaTemplate.send(NotificationTopics.COMMENT_REPLIED, new NotificationEventVO(
                memberId, replyNo, null, UUID.randomUUID().toString()
        ));
    }

    /**
     * [WHAT] 내 글에 댓글 알림, 카프카 토픽에 넣는 역할
     * [WHY] 프로듀서: 전달 / 컨슈머: 로직 -> 결합력 감소
     */
    public void publishPostCommented(int memberId,  int qnaNo, String qnaTitle) {
        kafkaTemplate.send(NotificationTopics.POST_COMMENTED, new NotificationEventVO(
                memberId, qnaNo, qnaTitle, UUID.randomUUID().toString()
        ));

        System.out.println("==============producer");
    }

}
