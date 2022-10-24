package oplossing.benchmarks;

import opgave.samplers.Sampler;
import oplossing.TwoThreeTree;

import java.util.Random;

public class TwoThreeTreeBenchMarker {

    public static final Random RG = new Random();

    public static void main(String[] args) {
        meanTimeAddNRandoms(1_000_000, 10);
    }

    public static void timeAdd_10_000_Randoms(){
        timeAddNRandoms(10_000);
    }

    public static double timeAddNRandoms(int n) {
        Sampler sampler = new Sampler(RG, n);
        TwoThreeTree<Integer> tree = new TwoThreeTree<>();

        long start = System.currentTimeMillis();
        for (Integer rand : sampler.getElements()) {
            tree.add(rand);
        }
        long time = System.currentTimeMillis() - start;
        double timeInSec = (double) time / 1000;
        System.out.println("Adding " + n + " elements took " + timeInSec + "s");
        return timeInSec;
    }

    public static void meanTimeAddNRandoms(int nNumbers, int nTrails) {
        double sum = 0;
        for (int i = 0; i < nTrails; i++) {
            System.out.print("Trail " + (i+1) + ": ");
            sum += timeAddNRandoms(nNumbers);
        }
        double mean = sum / nTrails;
        System.out.println(nTrails + " keer " + nNumbers + " getallen toevoegen geeft als gemiddelde tijd " + mean + "s");
    }

}
