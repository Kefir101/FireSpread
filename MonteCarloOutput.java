public class MonteCarloOutput {
    public static void main(String[] args) {
        int forestRows = 100;
        int forestColumns = 100;
        System.out.println("Tree Density   |   Average Hours   |  Percent Burned");
        for (double treeDensity = 0.005; treeDensity <= 1; treeDensity += 0.005) {
            double[] info = getBurnInfo(forestRows, forestColumns, treeDensity, 0);
            System.out.println(Math.round(treeDensity * 1000.0)/10.0 + ", " + Math.round(info[0]*1000.0)/1000.0 + ", " + Math.round(info[1] * 100 * 100.0) / 100.0);
        }
        System.out.println("Wind   |   Average Hours   |  Percent Burned");
        for (double wind = -1; wind <= 1; wind += 0.01) {
            double[] info = getBurnInfo(forestRows, forestColumns, 0.5, wind);
            System.out.println(Math.round(wind * 100.0) / 100.0 + ", " + info[0] + ", " + Math.round(info[1] * 100 * 100.0) / 100.0);
        }
    }
    private static double[] getBurnInfo(int forestRows, int forestColumns, double treeDensity, double wind) {
        int experiments = 30;
        double[] info = new double[2];
        for (int experiment = 0; experiment < experiments; experiment++) {
            Simulator sim = new Simulator(forestRows, forestColumns, treeDensity, wind);
            sim.runFire();
            info[0] += sim.getHours();
            info[1] += sim.getPercentageBurned();
        }
        return new double[]{info[0] / experiments, info[1] / experiments};
    }
}
