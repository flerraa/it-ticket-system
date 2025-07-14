package com.support.ticketing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.support.ticketing.model.User;
import com.support.ticketing.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    // Show profile
    @GetMapping
    public String showProfile(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "profile/view";
    }

    // Show edit form
    @GetMapping("/edit")
    public String showEditForm(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "profile/edit";
    }

    // Process profile update
    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute User updatedUser, Authentication auth) {
        User currentUser = userService.findByUsername(auth.getName());
        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setPassword(updatedUser.getPassword()); // No hashing yet
        userService.save(currentUser);

        return "redirect:/profile";
    }
}
