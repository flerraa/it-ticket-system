package com.support.ticketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.support.ticketing.model.Comment;
import com.support.ticketing.model.Ticket;
import com.support.ticketing.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findByTicket(Ticket ticket) {
        return commentRepository.findByTicket(ticket);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
