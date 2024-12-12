package com.example.be.controller;

import com.example.be.service.TicketPool;
import com.example.be.service.LogService;  // Import LogService to fetch logs
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ticket-status")
public class TicketStatusController {
    private final TicketPool ticketPool;
    private final LogService logService;  // Inject LogService to get logs

    // Constructor-based dependency injection for TicketPool and LogService
    public TicketStatusController(TicketPool ticketPool, LogService logService) {
        this.ticketPool = ticketPool;
        this.logService = logService;
    }

    // Endpoint to fetch ticket status
    @GetMapping("/")
    public int ticketStatus() {
        return ticketPool.getTicketCount();  // Return the current ticket count from TicketPool
    }

    // Endpoint to fetch logs
    @GetMapping("/logs")
    public List<String> getLogs() {
        return logService.getLogs();  // Fetch the logs from LogService
    }
}