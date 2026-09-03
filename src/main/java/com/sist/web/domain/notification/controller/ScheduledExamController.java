package com.sist.web.domain.notification.controller;


import com.sist.web.domain.exam.vo.ScheduledExamVO;
import com.sist.web.domain.notification.service.ScheduledExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduledExamController {
    private final ScheduledExamService scheduledExamService;

    @GetMapping("/scheduled-exam")
    public ResponseEntity<List<ScheduledExamVO>> getScheduledExamByMonth(@RequestParam int year, @RequestParam int month, Model model){
        List<ScheduledExamVO> result = scheduledExamService.getExamByMonth(year, month);
        return ResponseEntity.ok(result);
    }
}
