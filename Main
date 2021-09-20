public class Main {
    private static final int forestSize = 10;
    private static int[][] forest = new int[forestSize][forestSize];
    double fireSpreadHorizontalChance = 0.8;
    double fireSpreadDiagonalChance = 0.6;

    public static void main(String[] args) {
        forest = makeTreeDensity(forestSize, 0.5);
        printForest(forest);
        /*
        if value is:
        0 - no tree
        1 - tree
        2 - burning tree
        3 - ash
        */
    }
    private static int[][] makeTreeDensity(int size, double treeDensity) {
        int[][] forest = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Math.random() < treeDensity) {
                    forest[i][j] = 1;
                }
            }
        }
        return forest;
    }
    private static void printForest(int[][] forest) {
        for (int[] trees : forest) {
            for (int tree : trees) {
                System.out.print(tree + " ");
            }
            System.out.println();
        }
    }
}
