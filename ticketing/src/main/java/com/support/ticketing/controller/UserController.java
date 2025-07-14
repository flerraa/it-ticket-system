package com.support.ticketing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.support.ticketing.model.User;
import com.support.ticketing.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    // View all users
    @GetMapping
    public String listUsers(Model model, Authentication auth) {
        if (!isAdmin(auth)) return "redirect:/access-denied";

        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    // Show add user form
    @GetMapping("/add")
    public String showAddForm(Model model, Authentication auth) {
        if (!isAdmin(auth)) return "redirect:/access-denied";

        model.addAttribute("user", new User());
        return "users/add";
    }

    // Process add user form
    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, Authentication auth) {
        if (!isAdmin(auth)) return "redirect:/access-denied";

        user.setEnabled(true); // New users are enabled by default
        userService.save(user);
        return "redirect:/admin/users";
    }

    // Show edit user form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication auth) {
        if (!isAdmin(auth)) return "redirect:/access-denied";

        User user = userService.findById(id);
        if (user == null) return "redirect:/admin/users";

        model.addAttribute("user", user);
        return "users/edit";
    }

    // Process edit user form
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User updatedUser, Authentication auth) {
        if (!isAdmin(auth)) return "redirect:/access-denied";

        User existingUser = userService.findById(id);
        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword()); // No password hashing yet
            existingUser.setRole(updatedUser.getRole());
            existingUser.setEnabled(updatedUser.isEnabled());
            userService.save(existingUser);
        }
        return "redirect:/admin/users";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Authentication auth) {
        if (!isAdmin(auth)) return "redirect:/access-denied";

        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    // Enable or disable a user
    @GetMapping("/toggle/{id}")
    public String toggleUser(@PathVariable Long id, Authentication auth) {
        if (!isAdmin(auth)) return "redirect:/access-denied";

        User user = userService.findById(id);
        if (user != null) {
            user.setEnabled(!user.isEnabled());
            userService.save(user);
        }
        return "redirect:/admin/users";
    }

    // Check if user is admin
    private boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
