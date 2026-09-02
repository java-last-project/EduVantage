package com.sist.web.domain.book.controller; 

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    @GetMapping("/book/list")
    public String book_list() {
        return "book/list"; 
    }
}