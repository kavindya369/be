package com.example.be.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TicketPool {

    private static final Logger logger = LoggerFactory.getLogger(TicketPool.class);

    public static int MAX_CAPACITY = 1000;
    private final ConcurrentLinkedQueue<String> ticketQueue;
    private boolean isRunning;
    private final ScheduledExecutorService executorService;

    public TicketPool() {
        ticketQueue = new ConcurrentLinkedQueue<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
        isRunning = false;
        logger.info("TicketPool initialized with max capacity of {} tickets.", MAX_CAPACITY);
    }

    // Method to add tickets to the pool
    public synchronized void addTickets(int ticketCount) {
        int ticketsAdded = 0;
        for (int i = 0; i < ticketCount; i++) {
            if (ticketQueue.size() < MAX_CAPACITY) {
                ticketQueue.add("Ticket_" + (ticketQueue.size() + 1));
                ticketsAdded++;
            } else {
                logger.warn("TicketPool has reached maximum capacity of {} tickets.", MAX_CAPACITY);
                break;
            }
        }
        logger.info("{} tickets added to the pool. Current pool size: {}.", ticketsAdded, ticketQueue.size());
    }

    public void setMaxCapacity(int maxCapacity) {
        MAX_CAPACITY = maxCapacity;
        logger.info("Max capacity updated to {}.", MAX_CAPACITY);
    }

    // Method to remove tickets from the pool (Consumer logic)
    public synchronized String removeTicket() {
        String ticket = ticketQueue.poll();
        if (ticket != null) {
            logger.info("Ticket removed: {}. Current pool size: {}.", ticket, ticketQueue.size());
        } else {
            logger.warn("No tickets available to remove. Pool size is {}.", ticketQueue.size());
        }
        return ticket;
    }

    public int getTicketCount() {
        int count = ticketQueue.size();
        logger.info("Current ticket count: {}.", count);
        return count;
    }

    // Start the ticket release process
    public void startTicketRelease(int releaseRate) {
        if (!isRunning) {
            isRunning = true;
            executorService.scheduleAtFixedRate(() -> {
                if (ticketQueue.size() < MAX_CAPACITY) {
                    addTickets(releaseRate);
                }
            }, 0, 1, TimeUnit.SECONDS);
            logger.info("Ticket release process started with release rate: {} tickets/second.", releaseRate);
        } else {
            logger.warn("Ticket release process is already running.");
        }
    }

    // Stop the ticket release process
    public void stopTicketRelease() {
        if (isRunning) {
            isRunning = false;
            executorService.shutdownNow();
            logger.info("Ticket release process stopped.");
        } else {
            logger.warn("Ticket release process is not currently running.");
        }
    }

    // Reset the ticket pool
    public void resetTicketPool() {
        int previousSize = ticketQueue.size();
        ticketQueue.clear();
        addTickets(100);
        logger.info("Ticket pool reset. Previous size: {}, New size: {}.", previousSize, ticketQueue.size());
    }
}
