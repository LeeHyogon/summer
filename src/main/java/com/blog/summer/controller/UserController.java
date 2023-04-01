package com.blog.summer.controller;


import com.blog.summer.domain.User;
import com.blog.summer.dto.UserJoinDto;
import com.blog.summer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/join")
    public String joinForm(Model model){
        model.addAttribute("user", new UserJoinDto());
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserJoinDto user){
        userService.createUser(user);

        return "redirect:/login";
    }
}
