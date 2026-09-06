package com.sist.web.domain.notification.service;

import com.sist.web.domain.exam.vo.ScheduledExamVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduledExamService {
    Page<ScheduledExamVO> getExamByMonth(Integer memberId, int year, int month, Pageable pageable);
    void subscribeExam(int memberId, int examNo);
}
