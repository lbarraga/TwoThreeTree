package oplossing.benchmarks;

import opgave.SearchTree;
import opgave.samplers.Sampler;
import oplossing.BottomUpSemiSplayTwoThreeTree;
import oplossing.Ss233Node;
import oplossing.TwoThreeTree;

import java.util.List;
import java.util.Random;

public class TwoThreeTreeBenchMarker {

    public static final Random RG = new Random();
    public static int aantal = 0;
    public static int gem = 0;
    public static int visited = 0;

    public static void main(String[] args) {
        meanTimeAddNRandoms(1_000_000, 5);
    }

    public static double timeAddNRandoms(int n) {
        Sampler sampler = new Sampler(RG, n);
        List<Integer> samples = sampler.getElements();
        TwoThreeTree<Integer> tree = new TwoThreeTree<>();

        long start = System.currentTimeMillis();
        for (Integer rand : samples) {
            tree.add(rand);
        }
        long time = System.currentTimeMillis() - start;
        double timeInSec = (double) time / 1000;
        System.out.println(tree.maxDepth(tree.root));
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
