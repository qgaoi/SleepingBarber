package com.company;

import javax.swing.*;
import java.awt.*;

/**
 * Created by qigao on 2018/6/1.
 */
public class SleepingBarberMain extends JFrame {

    private static final Dimension LAYOUT_DIMENSION = new Dimension(1200, 900);

    public SleepingBarberMain() {
        super("Sleeping Barber Problem");
        SleepingBarberGUI gui = new SleepingBarberGUI();
        Container contentPane = getContentPane();
        contentPane.setSize(LAYOUT_DIMENSION);
        contentPane.add(gui);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {

        //SleepingBarber.getInstance().startShop();
        new SleepingBarberMain();
    }
}
