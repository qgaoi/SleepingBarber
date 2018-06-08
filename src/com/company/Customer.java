package com.company;

import java.util.concurrent.Semaphore;

/**
 * Created by qigao on 2018/6/1.
 */
public class Customer extends Thread {

    private int iD;
    private boolean hairCut = false;

    private Semaphore barber;
    private Semaphore customers;
    private Semaphore accessSeats;
    private WaitingRoom waitingRoom;

    public Customer() {

        SleepingBarber sleepingBarber = SleepingBarber.getInstance();
        this.iD = sleepingBarber.getNewCustomerID();
        this.barber = sleepingBarber.getBarbers();
        this.customers = sleepingBarber.getCustomers();
        this.accessSeats = sleepingBarber.getAccessSeats();
        this.waitingRoom = sleepingBarber.getWaitingRoom();
    }
    public void run() {
        long start = System.currentTimeMillis();
        while (!hairCut) {
            try {
                accessSeats.acquire();
                if (waitingRoom.getSeats() > 0) {
                    SleepingBarberGUI.updateWaitingRoomStatus(
                            "Customer " + this.iD + " enters in the waiting room."
                    );
                    waitingRoom.takeSeat();
                    accessSeats.release();
                    customers.release();
                    try {
                        barber.acquire();
                        accessSeats.acquire();
                        waitingRoom.freeSeat();
                        SleepingBarberGUI.updateWaitingRoomStatus(
                                "Customer " + this.iD + " leaves the waiting room"
                        );
                        SleepingBarberGUI.updateCuttingRoomStatus(
                                "Customer " + this.iD + " enters in the cutting room"
                        );
                        accessSeats.release();
                        hairCut = true;
                        long end = System.currentTimeMillis();
                        SleepingBarber.getInstance().reportWaitingTime(end - start);
                        this.get_haircut();

                    } catch (InterruptedException ex) {}
                }
                else  {
                    SleepingBarberGUI.updateWaitingRoomStatus(
                            "There are no free seats. Customer " + this.iD + " has left the barbershop."
                    );
                    SleepingBarber.getInstance().reportRejectedCustomer();
                    accessSeats.release();
                    hairCut = true;
                }
            }
            catch (InterruptedException ex) {}
        }

    }

    public void get_haircut(){
        try {
            sleep(SleepingBarber.getInstance().getCuttingTime());
        } catch (InterruptedException ex) {}
        SleepingBarberGUI.updateCuttingRoomStatus(
                "Haircut ends, customer " + this.iD + " leaves the barbershop"
        );
    }

}
