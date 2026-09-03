package com.sist.web.domain.notification.service;

import com.sist.web.domain.exam.vo.ScheduledExamVO;
import com.sist.web.domain.notification.mapper.ScheduledExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledExamService {
    private final ScheduledExamMapper scheduledExamMapper;

    public List<ScheduledExamVO> getExamByMonth(int year, int month){
        return scheduledExamMapper.getScheduledExamByMonth(year, month);
    }
}
