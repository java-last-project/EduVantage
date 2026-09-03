package com.sist.web.domain.notification.controller;


import com.sist.web.domain.exam.vo.ScheduledExamVO;
import com.sist.web.domain.notification.service.ScheduledExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ScheduledExamController {
    private final ScheduledExamService scheduledExamService;

    @GetMapping("/scheduled-exam")
    public ResponseEntity<Page<ScheduledExamVO>> getScheduledExamByMonth(@RequestParam int year,
                                                                         @RequestParam int month,
                                                                         @RequestParam(defaultValue = "0", required = false) int page){
        Pageable pageable = PageRequest.of(page,3, Sort.by("openDate").ascending());
        Page<ScheduledExamVO> result = scheduledExamService.getExamByMonth(year, month, pageable);
        return ResponseEntity.ok(result);
    }
}
