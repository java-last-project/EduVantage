package com.sist.web.domain.notification.mapper;

import com.sist.web.domain.exam.vo.ScheduledExamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduledExamMapper {
    //특정 달의 정기시험 일정 조회
    public List<ScheduledExamVO> getScheduledExamByMonth(@Param("year")int year, @Param("month")int month);
}
