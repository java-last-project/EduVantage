package com.sist.web.domain.notification.producer;

import com.sist.web.domain.notification.entity.NotificationTopics;
import com.sist.web.domain.notification.vo.NotificationEventVO;
import lombok.RequiredArgsConstructor;
//카프카 템플릿: 메시지를 발행할 때 쓰는 스프링 제공 도구
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * [WHAT] 수료 이벤트 발생 시, 카프카 토픽에 넣는 역할
 * [WHY] 프로듀서: 전달 / 컨슈머: 로직 -> 수료판정 로직이 이벤트 발행만 호출하면 되므로, 결합력 감소
 */
@Component
@RequiredArgsConstructor
public class CourseCompletedProducer {
    private final KafkaTemplate<String, NotificationEventVO> kafkaTemplate;

    public void publishCompletion(int memberId,  int courseNo, String courseTitle) {
        kafkaTemplate.send(NotificationTopics.COURSE_COMPLETED, new NotificationEventVO(
                memberId, courseNo, courseTitle, UUID.randomUUID().toString()
        ));
    }
}
