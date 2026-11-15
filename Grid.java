import java.util.*;

public class Grid {
  public enum Direction {
    NORTH,
    EAST,
    WEST,
    SOUTH,
  }

  private final int ROWS;
  private final int COLS;
  private final Direction CURRENT;
  private Cell[][] array;

  private final int[][] OIL_COORDS;
  private final int[][] SHORE_COORDS;
  private final int[] VESSEL_COORDS;
  private final int N_ROCKS;

  public Grid(
    int rows, int cols, int[][] oilCoords, int[][] shoreCoords, int[] vesselCoords, int nRocks
  ) {
    if (rows < 8 || rows > 99 || cols < 12 || cols > 99) {
      throw new ArithmeticException(
        String.format("Invalid grid dimensions: %dx%d. Expected 8x12-99x99.", rows, cols)
      );
    }

    this.ROWS = rows;
    this.COLS = cols;
    this.CURRENT = Direction.EAST;
    this.OIL_COORDS = oilCoords;
    this.SHORE_COORDS = shoreCoords;
    this.VESSEL_COORDS = vesselCoords;
    this.N_ROCKS = nRocks;
    array = generateClean(rows, cols);
    generate(OIL_COORDS, Cell.Status.OIL);
    generate(SHORE_COORDS, Cell.Status.SHORE);
    generate(VESSEL_COORDS, Cell.Status.VESSEL);
    generateRandom(N_ROCKS, Cell.Status.CLEAN, Cell.Status.ROCK);
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

  private Cell[][] generate(int[] coords, Cell.Status status) {
    int[][] a = {coords};
    return generate(a, status);
  }

  private Cell[][] generateRandom(int amount, Cell.Status targetStatus, Cell.Status status) {
    Random random = new Random();
    int i = 0;
    while (i < amount) {
      int r = random.nextInt(ROWS);
      int c = random.nextInt(COLS);
      if (array[r][c].getStatus() == targetStatus) {
        array[r][c] = new Cell(status);
        i++;
      }
    }
    return array;
  }

  public Direction getCurrent() {
    return CURRENT;
  }

  public int[][] getOilCoords() {
    return OIL_COORDS;
  }

  public int[] getVesselCoords() {
    return VESSEL_COORDS;
  }

  @Override
  public String toString() {
    String s = "  ";

    // x coords
    for (int c = 0; c < COLS; c++) {
      s += String.format("%3d", c);
    }
    s += "\n";

    for (int r = 0; r < ROWS; r++) {
      // y coords
      s += String.format("%2d", r);
      for (int c = 0; c < COLS; c++) {
        s += String.format("%3s", array[r][c]);
      }
      s += "\n";
    }

    return s;
  }
}
