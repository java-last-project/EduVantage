package com.sist.web.domain.notification.mapper;

import com.sist.web.domain.exam.vo.ScheduledExamVO;
import com.sist.web.domain.notification.vo.SubscribeExamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduledExamMapper {
    //특정 달의 정기시험 일정 조회
    List<ScheduledExamVO> getScheduledExamByMonth(
            @Param("memberId")Integer memberId,
            @Param("year")int year,
            @Param("month")int month,
            @Param("offset")int offset,
            @Param("size")int size);
    int countScheduledExamByMonth(@Param("year")int year,
                                  @Param("month")int month);
    void subscribeExam(@Param("memberId")int memberId, @Param("examNo")int examNo);
    SubscribeExamVO subscribeExamInfo(@Param("examNo")int examNo);
    List<SubscribeExamVO> findScheduledExamByDday(@Param("targetDate")int targetDate);

    //알림 아직 못받은 유저 조회
    List<Integer> findUnnotifiedSubscribers(@Param("examNo")int examNo, @Param("column")String column);
    void markNotified(@Param("memberId")int memberId, @Param("examNo")int examNo, @Param("column")String column);
}
