package com.company;

import java.util.Random;

/**
 * Created by qigao on 2018/6/1.
 */
public class Poisson {

    private static Random random = new Random();

    public static double getNextArrival(double lambda) {
        return exp(lambda);
    }

    public static double exp(double lambda) {
        if (!(lambda > 0.0))
            throw new IllegalArgumentException("lambda must be positive: " + lambda);
        return -Math.log(1 - random.nextDouble()) / lambda;
    }

}
