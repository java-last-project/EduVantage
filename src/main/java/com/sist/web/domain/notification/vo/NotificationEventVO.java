package com.sist.web.domain.notification.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [WHAT] 수료 이벤트에 필요한 정보 (서비스 -> 프로듀서)
 *          : 컨슈머가 알림을 구성할때 필요한 데이터
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEventVO {
    private int memberId;
    //알림에 연결 URL이 있는경우
    private int targetNo;
    //알림대상(수료알림:강의제목,도서구매알림:책제목 등)
    private String target;
    //멱등키
    String eventKey;
}
