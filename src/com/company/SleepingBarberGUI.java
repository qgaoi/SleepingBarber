package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by qigao on 2018/6/2.
 */
public class SleepingBarberGUI extends JPanel {
    private JButton startButton;
    private JButton stopButton;
    private JButton newCustomerButton;
    private JButton queueOfNewCustomerButton;

    private static JTextArea waitingRoomText;
    private static JTextArea cuttingRoomText;

    private static JTextField chairsInput;
    private static JTextField cuttingTimeInput;
    private static JTextField aveWaitingTimeOutput;
    private static JTextField rejectedCustomerOutput;

    private JPanel buttonPanel;
    private JPanel textPanel;

    public SleepingBarberGUI() {

        super();
        setPreferredSize(new Dimension(1200, 900));


        JPanel parameters = new JPanel(new GridLayout(1, 4));
        //parameters.setSize(new Dimension(600, 100));

        JLabel textChairs = new JLabel("Number of Chairs");
        //textChairs.setSize(new Dimension(100, 50));
        chairsInput = new JTextField("5");

        JLabel textCuttingTime = new JLabel("Cutting Time (ms)");
        cuttingTimeInput = new JTextField("5000");

        parameters.add(textChairs);
        parameters.add(chairsInput);
        parameters.add(textCuttingTime);
        parameters.add(cuttingTimeInput);
        parameters.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Parameters (optional)"));


        JPanel shopOpen = new JPanel(new GridBagLayout());
        //shopOpen.setSize(new Dimension(600, 200));

        startButton = new JButton("OPEN");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SleepingBarber.getInstance().startShop();
            }
        });

        stopButton = new JButton("CLOSE");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SleepingBarber.getInstance().stopAndGetWaitingTime();
            }
        });

        shopOpen.add(startButton);
        shopOpen.add(stopButton);
        shopOpen.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Barber Shop"));


        JPanel addCustomer = new JPanel(new GridBagLayout());
        //addCustomer.setSize(new Dimension(500, 100));

        newCustomerButton = new JButton("Add Customer");
        newCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer newCustomer = new Customer();
                newCustomer.start();
            }
        });
        queueOfNewCustomerButton = new JButton("Add Queue of Customers");
        queueOfNewCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread workingThread = new Thread() {
                    @Override
                    public void run() {
                        for(int i = 0; i < 20; i++) {
                            Customer newCustomer = new Customer();
                            newCustomer.start();
                            try {
                                TimeUnit.MILLISECONDS.sleep((long)Poisson.getNextArrival(1.0 / 2000));
                            } catch (InterruptedException ex) {}
                        }
                    }
                };
                workingThread.start();
            }
        });

        addCustomer.add(newCustomerButton);
        addCustomer.add(queueOfNewCustomerButton);
        addCustomer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Customers"));


        JPanel calculation = new JPanel(new GridLayout(2, 2));

        JLabel aveWaitingTime = new JLabel("Average Waiting Time (ms)");
        aveWaitingTimeOutput = new JTextField();

        JLabel lossRate = new JLabel("Loss Rate (%)");
        rejectedCustomerOutput = new JTextField();

        calculation.add(aveWaitingTime);
        calculation.add(aveWaitingTimeOutput);
        calculation.add(lossRate);
        calculation.add(rejectedCustomerOutput);
        calculation.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Analysis"));




        buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 0;
        buttonPanel.add(parameters, c);
        c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 1;
        buttonPanel.add(shopOpen, c);
        c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 2;
        buttonPanel.add(addCustomer, c);
        c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 3;
        buttonPanel.add(calculation, c);



        waitingRoomText = new JTextArea();
        JScrollPane waitingRoomStatus = new JScrollPane(waitingRoomText);
        waitingRoomStatus.setBorder(BorderFactory.createTitledBorder(
               BorderFactory.createEtchedBorder(), "Waiting Room Status"));

        cuttingRoomText = new JTextArea();
        JScrollPane cuttingRoomStatus = new JScrollPane(cuttingRoomText);
        cuttingRoomStatus.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Cutting Room Status"));

        textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2, 1));
        textPanel.add(waitingRoomStatus);
        textPanel.add(cuttingRoomStatus);


        this.setLayout(new GridLayout(1, 2));
        this.add(buttonPanel);
        this.add(textPanel);
    }


    public synchronized static void setWaitingRoomText(String newMsg) {
        waitingRoomText.setText(newMsg);
    }

    public synchronized static void setCuttingRoomText(String newMsg) {
        cuttingRoomText.setText(newMsg);
    }

    public synchronized static void updateWaitingRoomStatus(String newMsg) {
        String prevMsg = waitingRoomText.getText();
        setWaitingRoomText(prevMsg + "\n" + newMsg);
    }

    public synchronized static void updateCuttingRoomStatus(String newMsg) {
        String prevMsg = cuttingRoomText.getText();
        setCuttingRoomText(prevMsg + "\n" + newMsg);
    }

    public synchronized static void setAveWaitingTimeOutput(String newMsg) {
        aveWaitingTimeOutput.setText(newMsg);
    }

    public synchronized static void setRejectedCustomerOutput(String newMsg) {
        rejectedCustomerOutput.setText(newMsg);
    }

    public static int getNumOfChairs() {
        String str = chairsInput.getText();
        if(str.equals("")) {
            return 0;
        }
        return Integer.valueOf(str);
    }

    public static int getCuttingTime() {
        String str = cuttingTimeInput.getText();
        if(str.equals("")) {
            return 0;
        }
        return Integer.valueOf(str);
    }

}
