package com.sist.web.domain.book.controller; 

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    @GetMapping("/book/list")
    public String book_list(Model model) {
        model.addAttribute("main_html", "book/list");
    	return "main/main"; 
    }
    
    @GetMapping("/book/detail")
    public String book_detail(Model model) {
    	model.addAttribute("main_html", "book/detail");
    	return "main/main";
    }
    
}