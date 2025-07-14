package com.support.ticketing.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private String status = "OPEN"; // OPEN, IN_PROGRESS, CLOSED

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    // Constructors, Getters, Setters
    public Ticket() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public TaskPriority getPriority() { return priority; }

    public void setPriority(TaskPriority priority) { this.priority = priority; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public User getAssignedTo() { return assignedTo; }

    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
