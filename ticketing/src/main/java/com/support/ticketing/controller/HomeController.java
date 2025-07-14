package com.support.ticketing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.support.ticketing.model.User;
import com.support.ticketing.service.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homeRedirect(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                if (user.getRole().equals("ADMIN")) {
                    return "redirect:/admin/dashboard";
                } else if (user.getRole().equals("TECHNICIAN")) {
                    return "redirect:/user/dashboard";
                }
            }
        }
        return "redirect:/login";
    }
}
