package com.sist.web.domain.notification.repository;

import com.sist.web.domain.notification.entity.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
    //전체조회(읽음/안읽음 탭 고민필요)
    //Page<Notifications> findByOrderByRegdateDesc();

    //안읽음만(최근 10개)
    //읽음만(최근 10개)
    List<Notifications> findByIsReadOrderByRegdateDesc(String isRead);

    //뱃지 (카운트)
    List<Notifications> countByIsRead(String isRead);
}
