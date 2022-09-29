package com.nicole.springbootwebsocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class ChatController {
    private AtomicInteger idproducer = new AtomicInteger();

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("username", "user" + idproducer.getAndIncrement());
        return "index";
    }
}
