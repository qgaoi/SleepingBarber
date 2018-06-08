package com.company;

import java.util.concurrent.Semaphore;

/**
 * Created by qigao on 2018/6/1.
 */
public class Barber extends Thread {

    private int iD;
    private Semaphore barber;
    private Semaphore customers;
    private boolean shouldStop;

    public Barber(int i) {

        this.iD = i;
        SleepingBarber sleepingBarber = SleepingBarber.getInstance();
        this.barber = sleepingBarber.getBarbers();
        this.customers = sleepingBarber.getCustomers();

    }

    public void run() {
        while(!shouldStop) {
            try {
                barber.release();
                customers.acquire();
                this.cutHair();
            } catch (InterruptedException ex) {}
        }
    }


    public void cutHair(){
        SleepingBarberGUI.updateCuttingRoomStatus("The barber " + this.iD + " is cutting hair");
        try {
            sleep(SleepingBarber.getInstance().getCuttingTime());
        } catch (InterruptedException ex){ }
    }

    public synchronized void setShouldStop(boolean stop) {
        shouldStop = stop;
    }
}
