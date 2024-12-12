package com.example.be.controller;

import com.example.be.config.TicketConfig;
import com.example.be.service.TicketPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket-config")
public class TicketConfigController {

    private final TicketPool ticketPool;
    private final File configFile = new File("config.json");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TicketConfigController(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Configure the ticket pool with initial settings and save to config.json.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<TicketConfig> configureTicketPool(@RequestBody Map<String, Integer> config) {
        try {
            int totalTickets = config.getOrDefault("totalTickets", 0);
            int ticketReleaseRate = config.getOrDefault("ticketReleaseRate", 0);
            int customerRetrievalRate = config.getOrDefault("customerRetrievalRate", 0);
            int maxTicketCapacity = config.getOrDefault("maxTicketCapacity", 1000);

            ticketPool.setMaxCapacity(maxTicketCapacity);
            ticketPool.addTickets(totalTickets);

            TicketConfig response = new TicketConfig(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
            objectMapper.writeValue(configFile, response);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Retrieve the saved ticket configuration from config.json.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<TicketConfig> getTicketConfig() {
        try {
            if (!configFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            TicketConfig config = objectMapper.readValue(configFile, TicketConfig.class);
            return ResponseEntity.ok(config);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

//    /**
//     * Get the current status of the ticket pool.
//     */
//    @CrossOrigin(origins = "http://localhost:4200")
//    @GetMapping("/ticket-status")
//    public ResponseEntity<Integer> getTicketStatus() {
//        int totalTickets = ticketPool.getTicketCount();
//        return ResponseEntity.ok(totalTickets);
//    }
}
