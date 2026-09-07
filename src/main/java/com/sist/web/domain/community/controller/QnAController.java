package com.sist.web.domain.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sist.web.domain.community.service.QnaService;
import com.sist.web.domain.community.vo.QnaBoardVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QnAController {
	private final QnaService qService;
	
	@GetMapping("/qna/list")
	public String qna_list(Model model) {
		model.addAttribute("main_html","community/qna/list");
		return "main/main";
	}
	
	@GetMapping("/qna/detail")
	public String qna_detail(Model model,@RequestParam("no")int no) {
		QnaBoardVO vo=qService.qnaDetail(no);
		model.addAttribute("vo", vo);
		model.addAttribute("main_html","community/qna/detail");
		return "main/main";
	}
	
	@GetMapping("/qna/insert")
	public String qna_insert(Model model) {
		QnaBoardVO vo=new QnaBoardVO();
		model.addAttribute("vo", vo);
		model.addAttribute("main_html","community/qna/form");
		return "main/main";
	}
	
	@PostMapping("/qna/insert_ok")
	public String qna_insert_ok(@ModelAttribute QnaBoardVO vo) {
		qService.qnaInsert(vo);
		return "redirect:/qna/detail?no="+vo.getNo();
	}
	
	@GetMapping("/qna/update")
	public String qna_update(Model model,@RequestParam("no")int no) {
		QnaBoardVO vo=qService.qnaDetail(no);
		model.addAttribute("vo", vo);
		model.addAttribute("main_html","community/qna/form");
		return "main/main";
	}
	
	@PostMapping("/qna/update_ok")
	public String qna_update_ok(RedirectAttributes ra,@ModelAttribute QnaBoardVO vo) {
		boolean sCheck=qService.qnaUpdate(vo);
		if(!sCheck) {
			ra.addAttribute("msg", "답변 완료된 QnA는 수정하실 수 없습니다!");
		}
		return "redirect:/qna/detail?no="+vo.getNo();
	}
	
	@GetMapping("/qna/delete")
	public String qna_delete(RedirectAttributes ra,@RequestParam("no")int no) {
		boolean sCheck=qService.qnaDelete(no);
		if(!sCheck) {
			ra.addAttribute("msg", "답변 완료된 QnA는 삭제하실 수 없습니다!");
			return "redirect:/qna/detail?no="+no;
		}
		return "redirect:/qna/list";
	}
	
}
