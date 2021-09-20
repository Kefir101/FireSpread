public class simulateFire {
    private static final int forestSize = 10;
    private static int[][] forest = new int[forestSize][forestSize];
    private static final double fireSpreadHorizontalChance = 0.8;
    private static final double fireSpreadDiagonalChance = 0.6;
    public static void main(String[] args) {
        forest = makeTreeDensity(forestSize, 0.5);
        /*
        if value is:
        0 - no tree
        1 - tree
        2 - burning tree
        3 - ash
        */
        spreadFire();
    }
    private static void spreadFire(){
        int hours = 100;
        boolean setFireToTree = false;
        while(!setFireToTree){
            int row = (int)(Math.random()*forestSize);
            int col = (int)(Math.random()*forestSize);
            if(isInBound(forest, row, col) && forest[row][col] == 1){
                forest[row][col] = 2;
                setFireToTree = true;
            }
        }
        for (int hour = 0; hour < hours; hour++) {
            printForest(forest);
            System.out.println("--------------------------");
            for (int i = 0; i < forestSize; i++) {
                for (int j = 0; j < forestSize; j++) {
                    if(forest[i][j] == 2){
                        for (int k = i-1; k <= i+1; k++) {
                            for (int l = j-1; l <= j+1; l++) {
                                if(isInBound(forest, k, l) && forest[k][l] == 1){ //is in bounds and is tree
                                    int indexDiff = Math.abs(i - k) + Math.abs(j - l);
                                    if(indexDiff == 1){ //horizontal tree
                                        double fireChance = Math.random();
                                        if(fireChance < fireSpreadHorizontalChance) forest[k][l] = 2;
                                    }
                                    if(indexDiff == 2){ //diagonal tree
                                        double fireChance = Math.random();
                                        if(fireChance < fireSpreadDiagonalChance) forest[k][l] = 2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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
    private static boolean isInBound(int[][] forest, int row, int col){
        return row >= 0 && col >= 0 && row < forest.length && col < forest[0].length;
    }
}

