package com.sist.web.domain.notification.producer;

import org.springframework.kafka.core.KafkaTemplate;

/**
 * [WHAT] 수료 이벤트 발생 시, 카프카 토픽에 넣는 역할
 * [WHY] 프로듀서: 전달 / 컨슈머: 로직 -> 수료판정 로직이 이벤트 발행만 호출하면 되므로, 결합력 감소
 */
public class CourseCompletedProducer {
    //private final KafkaTemplate
}
