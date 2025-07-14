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
import org.springframework.web.bind.annotation.RequestParam;

import com.support.ticketing.model.TaskPriority;
import com.support.ticketing.model.Ticket;
import com.support.ticketing.model.User;
import com.support.ticketing.service.TicketService;
import com.support.ticketing.service.UserService;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    // View all tickets (admin only)
    @GetMapping("/all")
    public String viewAllTickets(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (!user.getRole().equals("ADMIN")) {
            return "redirect:/access-denied";
        }
        model.addAttribute("tickets", ticketService.findAll());
        return "tickets/list";
    }

    // View technician's assigned tickets
    @GetMapping("/my")
    public String viewAssignedTickets(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("tickets", ticketService.findByAssignedTo(user));
        return "tickets/list";
    }

    // View ticket details
    @GetMapping("/view/{id}")
    public String viewTicket(@PathVariable Long id, Model model, Authentication auth) {
        Ticket ticket = ticketService.findById(id);
        if (ticket == null) return "redirect:/tickets/my";

        User currentUser = userService.findByUsername(auth.getName());
        if (!currentUser.getRole().equals("ADMIN") &&
            (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(currentUser.getId()))) {
            return "redirect:/access-denied";
        }

        model.addAttribute("ticket", ticket);
        return "tickets/view";
    }

    // Add ticket (admin only)
    @GetMapping("/add")
    public String showAddForm(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (!user.getRole().equals("ADMIN")) {
            return "redirect:/access-denied";
        }

        model.addAttribute("ticket", new Ticket());
        model.addAttribute("technicians", userService.findAll()); // To assign
        model.addAttribute("priorities", TaskPriority.values());
        return "tickets/add";
    }

    @PostMapping("/add")
    public String submitAddForm(@ModelAttribute Ticket ticket, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (!user.getRole().equals("ADMIN")) {
            return "redirect:/access-denied";
        }

        ticketService.save(ticket);
        return "redirect:/tickets/all";
    }

    // Edit ticket (admin only)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (!user.getRole().equals("ADMIN")) {
            return "redirect:/access-denied";
        }

        Ticket ticket = ticketService.findById(id);
        if (ticket == null) return "redirect:/tickets/all";

        model.addAttribute("ticket", ticket);
        model.addAttribute("technicians", userService.findAll());
        model.addAttribute("priorities", TaskPriority.values());
        return "tickets/edit";
    }

    @PostMapping("/edit/{id}")
    public String submitEditForm(@PathVariable Long id, @ModelAttribute Ticket ticket, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (!user.getRole().equals("ADMIN")) {
            return "redirect:/access-denied";
        }

        ticket.setId(id);
        ticketService.save(ticket);
        return "redirect:/tickets/all";
    }

    // Delete ticket (admin only)
    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (!user.getRole().equals("ADMIN")) {
            return "redirect:/access-denied";
        }

        ticketService.deleteById(id);
        return "redirect:/tickets/all";
    }

    // Technician: update status of their own tickets
    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, Authentication auth) {
        Ticket ticket = ticketService.findById(id);
        if (ticket == null) return "redirect:/tickets/my";

        User user = userService.findByUsername(auth.getName());
        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getId().equals(user.getId())) {
            ticket.setStatus(status);
            ticketService.save(ticket);
        }

        return "redirect:/tickets/view/" + id;
    }
}
