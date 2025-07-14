package com.support.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.support.ticketing.model.Ticket;
import com.support.ticketing.model.User;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByAssignedTo(User user);
}
