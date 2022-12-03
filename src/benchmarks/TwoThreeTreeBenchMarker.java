package benchmarks;

import opgave.SearchTree;
import opgave.samplers.Sampler;
import opgave.samplers.ZipfSampler;
import oplossing.BottomUpSemiSplayTwoThreeTree;
import oplossing.Ss233Node;
import oplossing.TopDownSemiSplayTwoThreeTree;
import oplossing.TwoThreeTree;

import java.util.List;
import java.util.Random;

public class TwoThreeTreeBenchMarker {

    private static final int nTrails = 5;
    private static final int n = 1_000_000;

    @FunctionalInterface
    private interface TreeOperation {
        boolean doOperation(SearchTree<Integer> tree, Integer integer);
    }

    public static final Random RG = new Random();

    public static void main(String[] args) {
        benchMarkSampler(new Sampler(RG, n), "Normal Sampler");
        benchMarkSampler(new ZipfSampler(RG, n), "Zipf Sampler");
    }

    public static void benchMarkSampler(Sampler sampler, String samplerUitleg) {
        prettyPrint(samplerUitleg);
        benchMarkTree(new TwoThreeTree<>(), sampler, "TwoThreeTree");
        benchMarkTree(new BottomUpSemiSplayTwoThreeTree<>(), sampler, "BottomUpSemiSplayTwoThreeTree");
        benchMarkTree(new TopDownSemiSplayTwoThreeTree<>(), sampler, "TopDownSemiSplayTwoThreeTree");
    }

    public static void benchMarkTree(SearchTree<Integer> searchTree, Sampler sampler, String treeName) {
        System.out.println(" ================================ " + treeName + " Benchmark ================================");
        benchmarkSingleOperationMeanTime(searchTree, SearchTree::add, "Toevoegen", sampler);
        benchmarkSingleOperationMeanTime(searchTree, SearchTree::contains, "Contains", sampler);
        benchmarkSingleOperationMeanTime(searchTree, SearchTree::remove, "Verwijderen", sampler);
    }

    public static void benchmarkSingleOperationMeanTime(SearchTree<Integer> tree, TreeOperation treeOperation, String uitleg, Sampler sampler) {
        System.out.println(" *** Operation: " + uitleg + " on " + n + " elements");
        double sum = 0;
        for (int i = 0; i < nTrails; i++) {
            sum += benchMarkSingleOperation(tree, treeOperation, sampler);
        }
        double mean = sum / nTrails;
        System.out.println(uitleg + " " + n + " elements: mean time " + mean + "s.");
        System.out.println();
    }

    public static double benchMarkSingleOperation(SearchTree<Integer> tree, TreeOperation treeOperation, Sampler sampler) {
        List<Integer> samples = sampler.sample(n);

        long start = System.currentTimeMillis();
        for (Integer rand : samples) {
            treeOperation.doOperation(tree, rand);
        }
        long time = System.currentTimeMillis() - start;
        return (double) time / 1000;
    }

    public static void prettyPrint(String string) {
        System.out.println();
        System.out.println("***********************************************************");
        System.out.println("|                     " + string);
        System.out.println("***********************************************************");
        System.out.println();
    }

}
