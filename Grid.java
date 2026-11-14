import java.util.*;

public class Grid {
  public enum Direction {
    NORTH,
    EAST,
    WEST,
    SOUTH,
  }

  private int rows;
  private int cols;
  private Cell[][] array;
  private Direction current;

  private final int[][] OIL_COORDS = {
    {4, 1}, {5, 1}, {4, 2}, {5, 2}, {6, 2}, {5, 3}, {7, 4}, {8, 4}, {7, 5}, {8, 5}, {9, 5}, {8, 6}
  };
  private final int[][] SHORE_COORDS = {
    {10, 0}, {11, 0}, {11, 1}, {10, 2}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {10, 6}, {11, 6},
    {0, 7}, {1, 7}, {2, 7}, {9, 7}, {10, 7}, {11, 7}
  };
  private final int[][] VESSEL_COORDS = {{0, 4}};
  private final int N_ROCKS = 6;

  public Grid(int rows, int cols) {
    if ((rows <= 0 && rows >= 100) || (cols <= 0 && cols >= 100)) {
      return;
    }

    this.rows = rows;
    this.cols = cols;
    this.current = Direction.EAST;
    array = generateClean(rows, cols);
    generate(OIL_COORDS, Cell.Status.OIL);
    generate(SHORE_COORDS, Cell.Status.SHORE);
    generate(VESSEL_COORDS, Cell.Status.VESSEL);
    generateRandom(N_ROCKS, Cell.Status.CLEAN, Cell.Status.ROCK);
  }

  public Grid() {
    this(8, 12);
  }

  private Cell[][] generateClean(int rows, int cols) {
    Cell[][] array = new Cell[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        array[r][c] = new Cell();
      }
    }
    return array;
  }

  private Cell[][] generate(int[][] coords, Cell.Status status) {
    for (int[] i : coords) {
      array[i[1]][i[0]].setStatus(status);
    }
    return array;
  }

  private Cell[][] generateRandom(int amount, Cell.Status targetStatus, Cell.Status status) {
    Random random = new Random();
    int i = 0;
    while (i < amount) {
      int r = random.nextInt(rows);
      int c = random.nextInt(cols);
      if (array[r][c].getStatus() == targetStatus) {
        array[r][c] = new Cell(status);
        i++;
      }
    }
    return array;
  }

  public Direction getCurrent() {
    return current;
  }

  @Override
  public String toString() {
    String s = "  ";

    // x coords
    for (int c = 0; c < cols; c++) {
      s += String.format("%3d", c);
    }
    s += "\n";

    for (int r = 0; r < rows; r++) {
      // y coords
      s += String.format("%2d", r);
      for (int c = 0; c < cols; c++) {
        s += String.format("%3s", array[r][c]);
      }
      s += "\n";
    }

    return s;
  }
}
