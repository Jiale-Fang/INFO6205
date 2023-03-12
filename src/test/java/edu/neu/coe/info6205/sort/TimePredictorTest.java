package edu.neu.coe.info6205.sort;

import edu.neu.coe.info6205.sort.elementary.HeapSort;
import edu.neu.coe.info6205.sort.linearithmic.MergeSort;
import edu.neu.coe.info6205.sort.linearithmic.QuickSort_DualPivot;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;
import edu.neu.coe.info6205.util.PrivateMethodTester;
import edu.neu.coe.info6205.util.StatPack;
import org.junit.Test;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public class TimePredictorTest {

    private final String HeapSort = "HeapSort";

    private final String MergeSort = "MergeSort";

    private final String QuicksortDualPivot = "QuicksortDualPivot";

    @Test
    public void testSortBenchMark() {
        String sortingMethod = QuicksortDualPivot;
        for (int i = 1000; i <= 128000; i *= 2) {
            System.out.println("The length of Array is: " + i);
            int n = i;
            int m = 20;
            Supplier<Integer[]> integersSupplier = getSupplier(n);
            //Calculate the time for sorting
            benchmarkIsInstrumented("false", integersSupplier, n, m, sortingMethod);
            //Count operations
            benchmarkIsInstrumented("true", integersSupplier, n, m, sortingMethod);
        }
    }

    private void benchmarkIsInstrumented(String isInstrumented, Supplier<Integer[]> integersSupplier, int n, int m, String sortingMethod) {
        Config config;
        if (sortingMethod.equals(MergeSort))
            config = Config.setupConfig2(isInstrumented, "0", "1", "", "", "false", "true");
        else
            config = Config.setupConfig(isInstrumented, "0", "1", "", "");
        Helper<Integer> helper = HelperFactory.create(sortingMethod, n, config);
        helper.init(n);
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        StatPack statPack = null;
        if (config.isInstrumented())
            statPack = (StatPack) privateMethodTester.invokePrivate("getStatPack");
        SortWithHelper<Integer> sorter = null;
        switch (sortingMethod) {
            case HeapSort:
                sorter = new HeapSort<>(helper);
                break;
            case MergeSort:
                sorter = new MergeSort<>(helper);
                break;
            case QuicksortDualPivot:
                sorter = new QuickSort_DualPivot<>(helper);
                break;
        }
        benchmark(config, statPack, sorter, sortingMethod, integersSupplier, m);
    }

    private void benchmark(Config config, StatPack statPack, SortWithHelper<Integer> sorter, String sortingMethod, Supplier<Integer[]> integersSupplier, int m) {
        double avgTime = new Benchmark_Timer<Integer[]>(
                sortingMethod,
                (xs) -> {
                    Integer[] copy = Arrays.copyOf(xs, xs.length);
                    sorter.preProcess(xs);
                    return copy;
                },
                (xs) -> sorter.sort(xs, false), //Copy the array in the preFunction
                sorter::postProcess
        ).runFromSupplier(integersSupplier, m);
        if (!config.isInstrumented()) {
            System.out.println("avgTime in milliseconds: " + avgTime);
        }
        if (config.isInstrumented()) {
            assert statPack != null;
            System.out.println("Compares: " + (int) statPack.getStatistics(InstrumentedHelper.COMPARES).mean());
            System.out.println("Swaps: " + (int) statPack.getStatistics(InstrumentedHelper.SWAPS).mean());
            System.out.println("Hits: " + (int) statPack.getStatistics(InstrumentedHelper.HITS).mean());
            System.out.println();
        }
    }

    private Supplier<Integer[]> getSupplier(int n) {
        final Random random = new Random();
        return () -> {
            Integer[] result = (Integer[]) Array.newInstance(Integer.class, n);
            for (int i = 0; i < n; i++) result[i] = random.nextInt();
            return result;
        };
    }

}
