package com.example.be.service;

public class Customer implements Runnable {

    private final int customerId;
    private final int retrievalInterval;
    private final TicketPool ticketPool;

    public Customer(int customerId, int retrievalInterval, TicketPool ticketPool) {
        this.customerId = customerId;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (true) {
            String ticket = ticketPool.removeTicket();
            if (ticket != null) {
                System.out.println("Customer " + customerId + " purchased " + ticket);
            } else {
                System.out.println("Customer " + customerId + " found no tickets available.");
            }
            try {
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
