package edu.neu.coe.info6205.union_find;

import java.util.Random;
import java.util.Scanner;

public class UnionFindClient {
    private static final Random random = new Random();

    private static int getInput() {
        System.out.println("Please enter a number as the range of random numbers");
        Scanner input = new Scanner(System.in);
        while (!input.hasNextInt()) {
            System.out.println("Please enter an Integer number");
            input = new Scanner(System.in);
        }
        int n = input.nextInt();
        input.close();
        return n;
    }

    /**
     * Connect n pairs
     *
     * @param n number of pairs
     * @return number of executions
     */
    private static int count(int n) {
        UF_HWQUPC uf = new UF_HWQUPC(n, false);
        int connectionCnt = 0;    //How many times have taken in order to connect n nodes which have different roots
        int num1, num2;
        while (uf.components() > 1) {
            num1 = random.nextInt(n);
            num2 = random.nextInt(n);
            uf.connect(num1, num2);
            connectionCnt++;
        }
        return connectionCnt;
    }

    private static int getMeanConnections(int n) {
        int totalConnectionCnt = 0;
        int times = 100;
        for (int i = 0; i < times; i++) {
            totalConnectionCnt += UnionFindClient.count(n);
        }
        return totalConnectionCnt / times;
    }

    private static int calculate(int num) {
        return (int) (0.5 * num * Math.log(num));
    }

    public static void main(String[] args) {
//        int n = getInput();
        System.out.println("  N: " + "The number of connection: " + "  1/2Nln(N): ");
        for (int i = 100; i <= 10000; i += 100) {
            System.out.printf("%4d%14d%22d", i, getMeanConnections(i), calculate(i));
            System.out.println();
        }
    }
}
