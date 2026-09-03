package com.sist.web.domain.notification.entity;

/**
 * [WHAT] 카프카 토픽 이름을 enum으로 관리
 * [WHY] 프로듀서와 컨슈머는 토픽으로 연결되므로, 오타 차단
 *          - enum으로 관리하면 카프카 리스너 어노테이션에서 토픽 못꺼냄.
 */
public class NotificationTopics {
    public static final String COURSE_COMPLETED = "course-completed";
}
