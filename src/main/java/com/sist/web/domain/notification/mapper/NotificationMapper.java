package com.sist.web.domain.notification.mapper;

import com.sist.web.domain.notification.vo.NotificationVO;
import com.sist.web.domain.notification.vo.SubscribeExamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;


@Mapper
public interface NotificationMapper {
    List<NotificationVO> findRecentNotifications(@Param("memberId") int memberId, @Param("targetDate") Date targetDate);
    void markAllAsRead(@Param("nos") List<Integer> nos);
    @Update("""
    UPDATE notifications SET is_read = 'Y'
    WHERE no = #{no}
    """)
    void markAsRead(@Param("no")int no);
}
