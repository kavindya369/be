package com.example.be.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private List<String> logs = new ArrayList<>();

    public LogService() {
        logs.add("Vendor added 10 tickets");
        logs.add("Customer purchased 5 tickets");
        logger.info("LogService initialized with default logs.");
    }

    public List<String> getLogs() {
        logger.info("Fetching logs: {}", logs);
        return logs;
    }

    public void addLog(String message) {
        logs.add(message);
        logger.info("New log added: {}", message);
    }
}
