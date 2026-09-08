package com.sist.web.domain.community.controller;

import com.sist.web.domain.community.entity.NoticeBoard;
import com.sist.web.domain.community.service.NoticeBoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class NoticeBoardController {
    private final NoticeBoardService nService;

    @GetMapping("/notice/list")
    public String noticeBoard(Model model){
        model.addAttribute("main_html","community/notice/list");
        return "main/main";
    }

    @GetMapping("/notice/detail")
    public String noticeDetail(Model model,@RequestParam("no")Integer no){
        model.addAttribute("vo",nService.noticeBoardDetail(no));
        model.addAttribute("main_html","community/notice/detail");
        return "main/main";
    }

    @GetMapping("/notice/insert")
    public String noticeInsert(Model model){
        model.addAttribute("main_html","community/notice/form");
        return "main/main";
    }

    @PostMapping("/notice/insert_ok")
    public String noticeInsert_ok(HttpSession session, @ModelAttribute NoticeBoard vo){
        Integer no=nService.noticeInsert((int)session.getAttribute("member_id"),vo);
        return "redirect:/notice/detail?no="+no;
    }

    @GetMapping("/notice/update")
    public String noticeUpdate(Model model,@RequestParam("no")Integer no){
        NoticeBoard vo=nService.noticeUpdateData(no);
        model.addAttribute("vo",vo);
        model.addAttribute("main_html","community/notice/form");
        return "main/main";
    }

    @PostMapping("/notice/update_ok")
    public String noticeUpdate_ok(@RequestParam("no") Integer no,
                                  @RequestParam("categoryNo") Integer categoryNo,
                                  @RequestParam("subject") String subject,
                                  @RequestParam("content") String content){
        nService.noticeUpdate(no,categoryNo,subject,content);
        return "redirect:/notice/detail?no="+no;
    }

    @GetMapping("/notice/delete")
    @Transactional
    public String noticeDelete(@RequestParam("no")Integer no){
        NoticeBoard vo=nService.noticeUpdateData(no);
        nService.noticeDelete(vo);
        return "redirect:/notice/list";
    }
}
