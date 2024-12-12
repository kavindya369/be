package com.example.be.controller;

import com.example.be.config.ControlPanelConfig;
import com.example.be.config.TicketConfig;
import com.example.be.service.TicketPool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-action")
public class ControlPanelController {

    private final TicketPool ticketPool;

    public ControlPanelController(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Start the ticket release process with the specified release rate.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/start")
    public ResponseEntity<String> startTicketProcess(@RequestBody ControlPanelConfig controlPanelConfig) {
        if (controlPanelConfig.getReleaseRate() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid release rate");
        }

        ticketPool.startTicketRelease(controlPanelConfig.getReleaseRate());
        return ResponseEntity.ok("Ticket process started");
    }

    /**
     * Stop the ticket release process.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/stop")
    public ResponseEntity<String> stopTicketProcess() {
        ticketPool.stopTicketRelease();
        return ResponseEntity.ok("Ticket process stopped.");
    }

    /**
     * Reset the ticket process and pool.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/reset")
    public ResponseEntity<String> resetTicketProcess() {
        ticketPool.resetTicketPool();
        return ResponseEntity.ok("Ticket process reset.");
    }

    /**
     * Get the current status of the ticket pool.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/status")
    public ResponseEntity<TicketConfig> getTicketStatus() {
        int totalTickets = ticketPool.getTicketCount();
        int releaseRate = 10;
        int customerRetrievalRate = 5;
        int maxCapacity = TicketPool.MAX_CAPACITY;

        TicketConfig response = new TicketConfig(totalTickets, releaseRate, customerRetrievalRate, maxCapacity);
        return ResponseEntity.ok(response);
    }
}
