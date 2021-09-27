import java.util.Arrays;

public class Simulator {
    private static double fireSpreadHorizontalChance = 0.8;
    private static double fireSpreadDiagonalChance = 0.6;
    private static final int hourBurnThreshold = 5;
    double treeDensity;
    int[][] forest;
    double hours = 0;
    int hoursWithoutBurns = 0;
    double wind;
    public Simulator(int r, int c, double treeDensity, double wind) {
        this.treeDensity = treeDensity;
        this.wind = wind;
        forest = makeTreeDensity(r, c, treeDensity);
    }
    public void runFire(){
        setFire();
        do{
            hours++;
            if(!propagateFire()) hoursWithoutBurns++;
            else hoursWithoutBurns = 0;
        }while(hoursWithoutBurns < hourBurnThreshold);
    }
    public double getHours(){
        return hours - hoursWithoutBurns;
    }
    public double getPercentageBurned(){
        double burnedTrees = 0;
        double totalSize = getHeight() * getWidth();
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if(forest[i][j] == 2) burnedTrees++;
            }
        }
        return burnedTrees/(totalSize*treeDensity);
    }
    public int[][] makeTreeDensity(int height, int width, double treeDensity) {
        int[][] forest = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (Math.random() < treeDensity) {
                    forest[i][j] = 1;
                }
            }
        }
        return forest;
    }
    public void setFire(){
        boolean setFireToTree = false;
        while(!setFireToTree){
            int row = (int)(Math.random()*getHeight());
            int col = (int)(Math.random()*getWidth());
            if(isInBound(row, col) && forest[row][col] == 1){
                forest[row][col] = 2;
                setFireToTree = true;
            }
        }
    }
    public boolean propagateFire(){
        int treesBurned = 0;
        int [][] forestClone = Arrays.stream(forest).map(int[]::clone).toArray(int[][]::new);
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if(forestClone[i][j] == 2){
                    for (int k = i-1; k <= i+1; k++) {
                        for (int l = j-1; l <= j+1; l++) {
                            if(isInBound(k, l) && forestClone[k][l] == 1){ //is in bounds and is tree
                                int indexDiff = Math.abs(i - k) + Math.abs(j - l);
                                if(indexDiff == 1){ //horizontal tree
                                    if(this.wind < 0 && l - j < 0 || this.wind > 0 && l - j > 0){
                                        fireSpreadHorizontalChance *= (1 + Math.abs(wind));
                                    }else{
                                        fireSpreadHorizontalChance /= (1 + Math.abs(wind));
                                    }
                                    double fireChance = Math.random();
                                    if(fireChance < fireSpreadHorizontalChance){
                                        forest[k][l] = 2;
                                        treesBurned++;
                                    }
                                }
                                if(indexDiff == 2){ //diagonal tree
                                    double fireChance = Math.random();
                                    if(fireChance < fireSpreadDiagonalChance) {
                                        forest[k][l] = 2;
                                        treesBurned++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//        System.out.println("forest: " + Arrays.deepToString(forest));
//        System.out.println("-------------");
//        System.out.println("clone: " + Arrays.deepToString(forestClone));
        return treesBurned > 0;
    }
    private boolean isInBound(int row, int col){
        return row >= 0 && col >= 0 && row < getHeight() && col < getWidth();
    }
    public int getWidth() {
        return forest[0].length;
    }
    public int getHeight() {
        return forest.length;
    }
    public int[][] getDisplayGrid() {
        return forest;
    }
    public void resetForest(){
        forest = new int[getHeight()][getHeight()];
    }
}
