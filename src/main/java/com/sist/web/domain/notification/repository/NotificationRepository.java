package com.sist.web.domain.notification.repository;

import com.sist.web.domain.notification.entity.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
    //최근 3일치 알림 데이터 조회
    List<Notifications> findByMemberIdAndRegdateAfterOrderByRegdateDesc(int memberId, Date regdate);

    //뱃지 (카운트)
    List<Notifications> countByMemberIdAndRegdateAfter(int memberId, Date regdate);
}
