package com.sist.web.domain.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/access-denied")
    public String accessDenied(Model model) {
        
        return "error/access-denied"; // templates/error/access-denied.html 연결
    }
    
    
}


