/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.randomwalk;

import java.util.Random;

public class RandomWalk {

    private int x = 0;
    private int y = 0;

    private final Random random = new Random();

    /**
     * Private method to move the current position, that's to say the drunkard moves
     *
     * @param dx the distance he moves in the x direction
     * @param dy the distance he moves in the y direction
     */
    private void move(int dx, int dy) {
        x += dx;
        y += dy;
        // END
    }

    /**
     * Perform a random walk of m steps
     *
     * @param m the number of steps the drunkard takes
     */
    private void randomWalk(int m) {
        //Random walk m steps
        while (m-- > 0) {
            randomMove();
        }
        // END
    }

    /**
     * Private method to generate a random move according to the rules of the situation.
     * That's to say, moves can be (+-1, 0) or (0, +-1).
     */
    private void randomMove() {
        boolean ns = random.nextBoolean();
        int step = random.nextBoolean() ? 1 : -1;
        move(ns ? step : 0, ns ? 0 : step);
    }

    /**
     * Method to compute the distance from the origin (the lamp-post where the drunkard starts) to his current position.
     *
     * @return the (Euclidean) distance from the origin to the current position.
     */
    public double distance() {
        return Math.sqrt(x * x + y * y);
        // END
    }

    /**
     * Perform multiple random walk experiments, returning the mean distance.
     *
     * @param m the number of steps for each experiment
     * @param n the number of experiments to run
     * @return the mean distance
     */
    public static double randomWalkMulti(int m, int n) {
        double totalDistance = 0;
        for (int i = 0; i < n; i++) {
            RandomWalk walk = new RandomWalk();
            walk.randomWalk(m);
            totalDistance = totalDistance + walk.distance();
        }
        return totalDistance / n;
    }

    /**
     * Calculate how many steps it takes to walk about k meters(Euclidean distance)
     *
     * @param d how many meters should walk
     * @return how many steps it should take
     */
    private int calculateStepsByDistance(int d) {
        int steps = 0;
        while (distance() < d) {
            randomMove();
            steps++;
        }
        return steps;
    }

    /**
     * Perform multiple random walk experiments, returning the mean steps when the man is k meters far from the origin(Euclidean distance)
     *
     * @param d how many meters far from the origin
     * @param n the number of experiments to run
     * @return the mean steps to walk when the man is k meters far from the origin
     */
    private static int calculateStepsByDistanceMulti(int d, int n) {
        int steps = 0;
        for (int i = 0; i < n; i++) {
            RandomWalk randomWalk = new RandomWalk();
            steps += randomWalk.calculateStepsByDistance(d);
        }
        return steps / n;
    }

    public static void main(String[] args) {
        int m = 1;
        int n = 1000;
        //Use six different values of m
        for (int i = 0; i < 8; i++) {
            double meanDistance = randomWalkMulti(m, n);
            System.out.println("In " + m + " steps, walks about " + meanDistance + " meters over " + n + " experiments");
            m *= 10;
        }

        //How many meters far from the origin(Euclidean distance)
        int d = 1;
        //Run four iterations
        for (int i = 0; i < 4; i++) {
            int steps = calculateStepsByDistanceMulti(d, n);
            System.out.println("In " + n + " experiments, it takes about " + steps + " steps to reach " + d + " meters far from the origin of coordinates");
            d *= 10;
        }
    }

}
