import java.util.Arrays;

public class Simulator {
    private static final int NO_TREE = 0;
    private static final int LIVING_TREE = 1;
    private static final int BURNING_TREE = 2;
    private static final double fireSpreadHorizontalChance = 0.8;
    private static final double fireSpreadDiagonalChance = 0.57;
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
                if(forest[i][j] == BURNING_TREE) burnedTrees++;
            }
        }
        return burnedTrees/(totalSize*treeDensity);
    }
    public int[][] makeTreeDensity(int height, int width, double treeDensity) {
        int[][] forest = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (Math.random() < treeDensity) {
                    forest[i][j] = LIVING_TREE;
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
            if(isInBound(row, col) && forest[row][col] == LIVING_TREE){
                forest[row][col] = BURNING_TREE;
                setFireToTree = true;
            }
        }
    }
    public boolean propagateFire(){
        int treesBurned = 0;
        int [][] forestClone = Arrays.stream(forest).map(int[]::clone).toArray(int[][]::new);
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if(forestClone[i][j] == BURNING_TREE){
                    treesBurned += burnNearbyTrees(i, j, forestClone);
                }
            }
        }
        return treesBurned > 0;
    }
    private int burnNearbyTrees(int i, int j, int[][] forestClone) {
        int treesBurned = 0;
        for (int k = i-1; k <= i+1; k++) {
            for (int l = j-1; l <= j+1; l++) {
                if(isInBound(k, l) && forestClone[k][l] == LIVING_TREE){
                    int indexDiff = Math.abs(i - k) + Math.abs(j - l);
                    double tempHorzChance = fireSpreadHorizontalChance;
                    double tempDiagChance = fireSpreadDiagonalChance;
                    if(indexDiff == 1){ //horizontal tree
                        if (this.wind < 0 && l - j < 0 || this.wind > 0 && l - j > 0) {
                            tempHorzChance *= (1 + Math.abs(wind));
                        } else {
                            tempHorzChance /= (1 + Math.abs(wind));
                        }
                        if(burnSingleNearbyTree(tempHorzChance, k, l)) treesBurned++;
                    }
                    if(indexDiff == 2){ //diagonal tree
                        if(burnSingleNearbyTree(tempDiagChance, k, l)) treesBurned++;
                    }
                }
            }
        }
        return treesBurned;
    }
    private boolean burnSingleNearbyTree(double burnChance, int row, int col){
        double fireChance = Math.random();
        if(fireChance < burnChance) {
            forest[row][col] = BURNING_TREE;
            return true;
        }
        return false;
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
