package benchmarks;

import opgave.SearchTree;
import opgave.samplers.Sampler;
import opgave.samplers.ZipfSampler;
import oplossing.BottomUpSemiSplayTwoThreeTree;
import oplossing.TopDownSemiSplayTwoThreeTree;
import oplossing.TwoThreeTree;

import java.util.List;
import java.util.Random;

public class TwoThreeTreeBenchMarker {

    private static final int nTrails = 1;
    private static final boolean prettyPrint = false;

    @FunctionalInterface
    private interface TreeOperation {
        boolean doOperation(SearchTree<Integer> tree, Integer integer);
    }

    public static final Random RG = new Random();

    public static void main(String[] args) {
        for (int i = 50_000; i < 5_000_000; i += 50_000) {
            BenchMarkCSVPrinter.print(benchMark(i));
        }
    }

    public static BenchMarkData benchMark(int n) {
        SamplerBenchMark normal = benchMarkSampler(new Sampler(RG, n), n, "Normal Sampler");
        SamplerBenchMark zipf   = benchMarkSampler(new ZipfSampler(RG, n), n, "Zipf Sampler");
        compare(normal, zipf);
        return new BenchMarkData(normal, zipf);
    }

    public static SamplerBenchMark benchMarkSampler(Sampler sampler, int n, String samplerUitleg) {
        prettyPrint(samplerUitleg);
        TreeBenchMark tTT  = benchMarkTree(new TwoThreeTree<>(), sampler, n, "TwoThreeTree");
        TreeBenchMark SSBO = benchMarkTree(new BottomUpSemiSplayTwoThreeTree<>(), sampler, n, "BottomUpSemiSplayTwoThreeTree");
        TreeBenchMark SSTD = benchMarkTree(new TopDownSemiSplayTwoThreeTree<>(), sampler, n, "TopDownSemiSplayTwoThreeTree");
        return new SamplerBenchMark(tTT, SSBO, SSTD);
    }

    public static TreeBenchMark benchMarkTree(SearchTree<Integer> searchTree, Sampler sampler, int n,  String treeName) {
        printIfPrettyPrint(" ================================ " + treeName + " Benchmark ================================");
        double add      = benchmarkSingleOperationMeanTime(searchTree, SearchTree::add, n, "Toevoegen", sampler);
        double contains = benchmarkSingleOperationMeanTime(searchTree, SearchTree::contains, n, "Contains", sampler);
        double remove   = benchmarkSingleOperationMeanTime(searchTree, SearchTree::remove, n, "Verwijderen", sampler);
        return new TreeBenchMark(add, contains, remove);
    }

    public static double benchmarkSingleOperationMeanTime(SearchTree<Integer> tree, TreeOperation treeOperation, int n, String uitleg, Sampler sampler) {
        printIfPrettyPrint(" *** Operation: " + uitleg + " on " + n + " elements");
        double sum = 0;
        for (int i = 0; i < nTrails; i++) {
            sum += benchMarkSingleOperation(tree, treeOperation, n, sampler);
        }
        double mean = sum / nTrails;
        printIfPrettyPrint(uitleg + " " + n + " elements: mean time " + mean + "s.");
        printIfPrettyPrint("");
        return mean;
    }

    public static double benchMarkSingleOperation(SearchTree<Integer> tree, TreeOperation treeOperation, int n,  Sampler sampler) {
        List<Integer> samples = sampler.sample(n);

        long start = System.currentTimeMillis();
        for (Integer rand : samples) {
            treeOperation.doOperation(tree, rand);
        }
        long time = System.currentTimeMillis() - start;
        return (double) time / 1000;
    }

    public static void prettyPrint(String string) {
        printIfPrettyPrint("");
        printIfPrettyPrint("***********************************************************");
        printIfPrettyPrint("|                     " + string);
        printIfPrettyPrint("***********************************************************");
        printIfPrettyPrint("");
    }

    public static void compare(SamplerBenchMark normal, SamplerBenchMark zipf){
        compareTree(normal.TTT(), zipf.TTT(), "TwoThreeTree");
        compareTree(normal.SSBO(), zipf.SSBO(), "BottomUpSemiSplayTwoThreeTree");
        compareTree(normal.SSTD(), zipf.SSTD(), "TopDownSemiSplayTwoThreeTree");
    }

    public static void compareTree(TreeBenchMark tbm1, TreeBenchMark tbm2, String treeString) {
        printIfPrettyPrint("---" + treeString + "=>  Operation: normal VS zipf");
        compareOne("Add", tbm1.add(), tbm2.add());
        compareOne("Contains", tbm1.contains(), tbm2.contains());
        compareOne("Remove", tbm1.remove(), tbm2.remove());
        printIfPrettyPrint("---");
    }

    public static void compareOne(String operation, double normal, double zipf) {
        printIfPrettyPrint(operation + ": " + normal + " VS " + zipf + "     (" + normal / zipf + "%)");
    }

    public static void printIfPrettyPrint(String s) {
        if (prettyPrint) {
            printIfPrettyPrint(s);
        }
    }

}
