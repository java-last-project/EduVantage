package com.sist.web.domain.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;
import com.sist.web.domain.community.service.FreeBoardService;
import com.sist.web.domain.community.vo.FreeBoardVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeBoardController {
	private final FreeBoardService fService;
	
	@GetMapping("/freeboard/list")
	public String freeboard_list(@RequestParam(value="page",defaultValue="1")int page,Model model) {
		model.addAttribute("main_html", "community/freeboard/list");
		return "main/main";
	}
	
	@GetMapping("/freeboard/detail")
	public String freeboard_detail(@RequestParam("no")int no,Model model) {
		FreeBoardVO vo=fService.freeBoardDetail(no);
		model.addAttribute("vo", vo);
		model.addAttribute("main_html", "community/freeboard/detail");
		return "main/main";
	}
}
