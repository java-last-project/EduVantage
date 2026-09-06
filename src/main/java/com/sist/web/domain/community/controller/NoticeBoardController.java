package com.sist.web.domain.community.controller;

import com.sist.web.domain.community.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NoticeBoardController {
    private final NoticeBoardService nService;

    @GetMapping("/notice")
    public String noticeBoard(Model model){
        model.addAttribute("main_html","community/notice/list");
        return "main/main";
    }
}
