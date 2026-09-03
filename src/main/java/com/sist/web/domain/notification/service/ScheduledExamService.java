package com.sist.web.domain.notification.service;

import com.sist.web.domain.exam.vo.ScheduledExamVO;
import com.sist.web.domain.notification.mapper.ScheduledExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledExamService {
    private final ScheduledExamMapper scheduledExamMapper;

    public Page<ScheduledExamVO> getExamByMonth(int year, int month, Pageable pageable){
        int offset = pageable.getPageNumber()*pageable.getPageSize();
        int size = pageable.getPageSize();
        List<ScheduledExamVO> list = scheduledExamMapper.getScheduledExamByMonth(year, month, offset, size);
        int totalCount = scheduledExamMapper.countScheduledExamByMonth(year, month);

        return new PageImpl<>(list, pageable, totalCount);
    }
}
