package com.sist.web.domain.community.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/freeboard/insert")
	public String freeboard_insert(Model model){
		model.addAttribute("main_html", "community/freeboard/form");
		return "main/main";
	}

	@PostMapping("/freeboard/insert_ok")
	public String freeboard_insert_ok(@ModelAttribute FreeBoardVO vo, HttpSession session){
		fService.freeBoardInsert(vo,session);
		return "redirect:/freeboard/detail?no="+vo.getNo();
	}

	@GetMapping("/freeboard/update")
	public String freeboard_update(@RequestParam("no")int no,Model model){
		FreeBoardVO vo=fService.freeBoardUpdateData(no);
		model.addAttribute("vo",vo);
		model.addAttribute("main_html", "community/freeboard/form");
		return "main/main";
	}

	@PostMapping("/freeboard/pwd_check")
	public String freeboard_pwd_check(@RequestParam("no")int no,@RequestParam("pwd")String pwd,@RequestParam("actionType")String actionType,Model model){
		boolean bCheck=fService.freeBoardPwdCheck(no,pwd);
		if(!bCheck){
			return "redirect:/freeboard/detail?no="+no+"&error=pwd";
		}else {
			if ("update".equals(actionType)) {
				FreeBoardVO vo=fService.freeBoardUpdateData(no);
				model.addAttribute("vo",vo);
				model.addAttribute("main_html","community/freeboard/form");
				return "main/main";
			} else {
				fService.freeBoardDelete(no);
				return "redirect:/freeboard/list";
			}
		}
	}

	@PostMapping("/freeboard/update_ok")
	public String freeboard_update_ok(@ModelAttribute FreeBoardVO vo){
		fService.freeBoardUpdate(vo);
		return "redirect:/freeboard/detail?no="+vo.getNo();
	}

	@PostMapping("/freeboard/delete")
	public String freeboard_delete(@RequestParam("no")int no){
		fService.freeBoardDelete(no);
		return "redirect:/freeboard/list";
	}
}
