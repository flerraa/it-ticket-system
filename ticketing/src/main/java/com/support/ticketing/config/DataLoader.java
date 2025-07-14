package com.support.ticketing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.support.ticketing.model.TaskPriority;
import com.support.ticketing.model.Ticket;
import com.support.ticketing.model.User;
import com.support.ticketing.service.TicketService;
import com.support.ticketing.service.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @PostConstruct
    public void loadData() {
        if (userService.findByUsername("admin") == null) {
            // Admin
            User admin = new User("admin", "admin123", "ADMIN");
            userService.save(admin);

            // Technicians
            User tech1 = new User("tech1", "pass123", "TECHNICIAN");
            User tech2 = new User("tech2", "pass123", "TECHNICIAN");
            userService.save(tech1);
            userService.save(tech2);

            // Sample tickets
            Ticket t1 = new Ticket();
            t1.setTitle("Printer not working");
            t1.setDescription("The office printer on floor 2 is jammed.");
            t1.setPriority(TaskPriority.MEDIUM);
            t1.setStatus("OPEN");
            t1.setAssignedTo(tech1);
            ticketService.save(t1);

            Ticket t2 = new Ticket();
            t2.setTitle("Email issue");
            t2.setDescription("User cannot receive emails.");
            t2.setPriority(TaskPriority.HIGH);
            t2.setStatus("OPEN");
            t2.setAssignedTo(tech2);
            ticketService.save(t2);
        }
    }
}
