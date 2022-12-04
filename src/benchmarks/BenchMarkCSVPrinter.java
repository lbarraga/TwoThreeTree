package benchmarks;

/**
 * Print csv values om te plakken in excel om grafieken te maken
 */
public class BenchMarkCSVPrinter {

    public static void print(BenchMarkData data) {
        printSamplerData(data.normal());
        printSamplerData(data.zipf());
        System.out.println();
    }

    public static void printSamplerData(SamplerBenchMark sd) {
        printData(sd.TTT().add(), sd.SSBO().add(), sd.SSTD().add());
        printData(sd.TTT().contains(), sd.SSBO().contains(), sd.SSTD().contains());
        printData(sd.TTT().remove(), sd.SSBO().remove(), sd.SSTD().remove());
    }

    public static void printData(double d1, double d2, double d3) {
        System.out.print(d1 + "," + d2 + "," + d3 + ",");
    }
}
