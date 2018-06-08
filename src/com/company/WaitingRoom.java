package com.company;

/**
 * Created by qigao on 2018/6/1.
 */
public class WaitingRoom {

    private int seats;

    public WaitingRoom(int seats) {
        this.seats = seats;
    }

    public void freeSeat() {
        seats++;
    }

    public void takeSeat() {
        seats--;
    }

    public void setSeats(int seats) {
        if(seats > 0) {
            this.seats = seats;
        }
    }

    public synchronized int getSeats() {
        return this.seats;
    }
}
