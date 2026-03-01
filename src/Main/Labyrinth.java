package Main;
import java.util.Random;

public class Labyrinth {
    private final int rows;
    private final int cols;
    private final int[][] grid;
    private final Random random = new Random();

    public static final int PATH = 0;
    public static final int WALL = 1;
    public static final int TRREASURE1 = 2;
    public static final int TRREASURE2 = 3;
    public static final int TRREASURE3 = 4;
    public static final int START = 5;
    public static final int END = 6;

    public final int treasureGenerated = random.nextInt(20) + 10;

    public Labyrinth(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];
        initializeGrid();
        generateLabyrinth(1, 1);
        ensureRandomPath();
        generateTreasure();
    }

    private void initializeGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = WALL;
            }
        }
    }

    private void generateTreasure() {
        grid[1][1] = START;
        grid[rows - 2][cols - 1] = END;

        int count = 0;
        while (count < treasureGenerated) {
            int i = random.nextInt(rows);
            int j = random.nextInt(cols);


            if (grid[i][j] == PATH && grid[i][j] != START && grid[i][j] != END) {
                int treasureType = random.nextInt(3) + 2;
                grid[i][j] = treasureType;
                count++;
            }
        }
    }

    private void generateLabyrinth(int x, int y) {
        grid[x][y] = PATH;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        shuffleArray(directions);

        for (int[] dir : directions) {
            int nx = x + dir[0] * 2;
            int ny = y + dir[1] * 2;
            if (isValidCell(nx, ny)) {
                grid[x + dir[0]][y + dir[1]] = PATH;
                generateLabyrinth(nx, ny);
            }
        }
    }

    private boolean isValidCell(int x, int y) {
        return x > 0 && x < rows - 1 && y > 0 && y < cols - 1 && grid[x][y] == WALL;
    }

    private void shuffleArray(int[][] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void ensureRandomPath() {
        int x = 1, y = 1;
        while (x < rows - 2 || y < cols - 2) {
            if (random.nextBoolean() && x < rows - 2) {
                grid[x][y] = PATH;
                x++;
            } else if (y < cols - 2) {
                grid[x][y] = PATH;
                y++;
            }
        }
        grid[rows - 2][cols - 2] = PATH;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getTreasureGenerated() {
        return treasureGenerated;
    }
}
