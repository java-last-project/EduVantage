package com.sist.web.domain.notification.mapper;

import com.sist.web.domain.notification.vo.NotificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface NotificationMapper {
    List<NotificationVO> findRecentNotifications(@Param("memberId") int memberId, @Param("targetDate") Date targetDate);
}
