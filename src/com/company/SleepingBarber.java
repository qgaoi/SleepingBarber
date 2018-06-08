package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by qigao on 2018/6/1.
 */
public class SleepingBarber {

    /* PREREQUISITES */
    private static SleepingBarber instance;

    private Semaphore customers;
    private Semaphore barbers;
    private Semaphore accessSeats;

    private static final int DEFAULT_CHAIRS = 5;
    private WaitingRoom waitingRoom;

    private int customerID;
    private int rejectedCustomer;

    private List<Barber> workingBarbers;
    private int cuttingTime = 5000;

    private List<Long> waitingTime;
    private double avgWaitingTime;


    private SleepingBarber() {
        super();
        customers = new Semaphore(0, true);
        barbers = new Semaphore(0);
        accessSeats = new Semaphore(1);
        waitingRoom = new WaitingRoom(DEFAULT_CHAIRS);
        waitingTime = new ArrayList<>();
        workingBarbers = new ArrayList<>();
        setCuttingTime();
    }

    public static synchronized SleepingBarber getInstance() {
        if (instance == null) {
            instance = new SleepingBarber();
        }
        return instance;
    }

    public void startShop(){

        waitingRoom.setSeats(SleepingBarberGUI.getNumOfChairs());

        Barber barber1 = new Barber(1);
        Barber barber2 = new Barber(2);
        workingBarbers.add(barber1);
        workingBarbers.add(barber2);
        barber1.start();
        barber2.start();

    }


    public synchronized void stopAndGetWaitingTime() {


        for (Barber barber : workingBarbers) {
            barber.setShouldStop(true);
        }
        if (waitingTime.size() == 0) {
            return;
        }
        long sum = 0;
        for (long time : waitingTime) {
            sum += time;
        }


        avgWaitingTime =  (double)sum / waitingTime.size();
        SleepingBarberGUI.setAveWaitingTimeOutput(String.valueOf((long)avgWaitingTime));
        SleepingBarberGUI.setRejectedCustomerOutput(String.valueOf((double)rejectedCustomer * 100 / (customerID)));
        System.out.println(SleepingBarberGUI.getCuttingTime());

        customerID = 0;
        resetWaitingTime();
        rejectedCustomer = 0;
        resetWorkingBarbers();
        barbers.drainPermits();
        customers.drainPermits();
        SleepingBarberGUI.setWaitingRoomText("");
        SleepingBarberGUI.setCuttingRoomText("");

    }

    public synchronized void reportWaitingTime(long time) {
        this.waitingTime.add(time);
    }

    public synchronized int getNewCustomerID() {
        return customerID++;
    }

    public synchronized void reportRejectedCustomer() {
        this.rejectedCustomer++;
    }

    public synchronized void addNewCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.start();
    }

    public Semaphore getCustomers() {
        return customers;
    }

    public Semaphore getBarbers() {
        return barbers;
    }

    public Semaphore getAccessSeats() {
        return accessSeats;
    }

    public WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

    private void resetWaitingTime() {
        this.waitingTime = new ArrayList<>();
    }

    private void resetWorkingBarbers() {
        this.workingBarbers = new ArrayList<>();
    }

    private void setCuttingTime() {
        int time = SleepingBarberGUI.getCuttingTime();
        if(time > 0) {
            cuttingTime = time;
        }
    }

    public int getCuttingTime() {
        return cuttingTime;
    }

    public synchronized double getAvgWaitingTime() {
        return avgWaitingTime;
    }
}
