/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public class InsertionSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * Constructor for any sub-classes to use.
     *
     * @param description the description.
     * @param N           the number of elements expected.
     * @param config      the configuration.
     */
    protected InsertionSort(String description, int N, Config config) {
        super(description, N, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(int N, Config config) {
        this(DESCRIPTION, N, config);
    }

    public InsertionSort(Config config) {
        this(new BaseHelper<>(DESCRIPTION, config));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        super(helper);
    }

    public InsertionSort() {
        this(BaseHelper.getHelper(InsertionSort.class));
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        for (int i = from + 1; i < to; i++) {
            int j = i;
            while (j > from && helper.less(xs[i], xs[j - 1])) {
                j--;
            }
            helper.swapInto(xs, j, i);
        }
        // END 
    }

    /**
     * Generate four types of arr
     *
     * @param type initial arr type
     * @param len    length
     * @return supplier
     */
    public static Supplier<Integer[]> initialArr(String type, int len) {
        switch (type) {
            case RANDOM_ARRAY:
                return getSupplier(0, len, 0, 0, len);
            case ORDERED_ARRAY:
                return getSupplier(0, 0, 0, len, len);
            case PARTIALLY_ORDERED_ARRAY:
                return getSupplier(0, len / 2, len / 2, len, len);
            case REVERSE_ORDERED_ARRAY:
                return () -> {
                    Integer[] result = (Integer[]) Array.newInstance(Integer.class, len);
                    for (int i = len - 1; i >= 0; i--) result[i] = len - 1 - i;
                    return result;
                };
        }
        return null;
    }

    public static double runBenchmarkTimer(Supplier<Integer[]> supplier, int m){
        return new Benchmark_Timer<Integer[]>(
                "insertionSort",
                (xs) -> Arrays.copyOf(xs, xs.length),
                InsertionSort::sort,
                null
        ).runFromSupplier(supplier, m);
    }

    /**
     * @param rStart generate random arr from rStart to rEnd
     * @param rEnd   end index
     * @param sStart generate sorted arr from sStart to sEnd
     * @param sEnd   end index
     * @param length arr length
     * @return supplier
     */
    private static Supplier<Integer[]> getSupplier(int rStart, int rEnd, int sStart, int sEnd, int length) {
        final Random random = new Random();
        final Supplier<Integer[]> integersSupplier = () -> {
            Integer[] result = (Integer[]) Array.newInstance(Integer.class, length);
            for (int i = rStart; i < rEnd; i++) result[i] = random.nextInt();
            for (int i = sStart; i < sEnd; i++) result[i] = i;
            return result;
        };
        return integersSupplier;
    }

    public static final String RANDOM_ARRAY = "Random Array";
    public static final String ORDERED_ARRAY = "Ordered Array";
    public static final String PARTIALLY_ORDERED_ARRAY = "Partially Ordered Array";
    public static final String REVERSE_ORDERED_ARRAY = "Reverse Ordered Array";

    public static final String DESCRIPTION = "Insertion sort";

    public static <T extends Comparable<T>> void sort(T[] ts) {
        new InsertionSort<T>().mutatingSort(ts);
    }

}
