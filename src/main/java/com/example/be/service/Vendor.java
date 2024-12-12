package com.example.be.service;

public class Vendor implements Runnable {

    private final int vendorId;
    private final int ticketsPerRelease;
    private final int releaseInterval;
    private final TicketPool ticketPool;

    public Vendor(int vendorId, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (true) {
            ticketPool.addTickets(ticketsPerRelease);
            System.out.println("Vendor " + vendorId + " added " + ticketsPerRelease + " tickets.");
            try {
                Thread.sleep(releaseInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
