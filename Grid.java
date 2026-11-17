import java.util.*;

public class Grid {
  public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST
  }

  private final int ROWS;
  private final int COLS;

  private Cell[][] array;
  private int[] vesselCoords;

  private final int N_ROCKS;
  public static final String RESET = "\u001B[0m";
  public static final String BLACK = "\u001B[30m";
  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";
  public static final String BLUE = "\u001B[34m";
  public static final String PURPLE = "\u001B[35m";
  public static final String CYAN = "\u001B[36m";
  public static final String WHITE = "\u001B[37m";

  public static final String BLACK_BACKGROUND = "\u001B[40m";
  public static final String RED_BACKGROUND = "\u001B[41m";
  public static final String GREEN_BACKGROUND = "\u001B[42m";
  public static final String YELLOW_BACKGROUND = "\u001B[43m";
  public static final String BLUE_BACKGROUND = "\u001B[44m";
  public static final String PURPLE_BACKGROUND = "\u001B[45m";
  public static final String CYAN_BACKGROUND = "\u001B[46m";
  public static final String WHITE_BACKGROUND = "\u001B[47m";

  public Grid(
    int rows, int cols, int[][] oilCoords, int[][] shoreCoords, int[] vesselCoords, int nRocks) {
    if (rows < 8 || rows > 99 || cols < 12 || cols > 99) {
      throw new ArithmeticException(
        String.format("Invalid grid dimensions: %dx%d. Expected 8x12-99x99.", rows, cols));
    }

    this.ROWS = rows;
    this.COLS = cols;
    this.vesselCoords = vesselCoords;
    this.N_ROCKS = nRocks;
    array = generateClean(rows, cols);
    generate(oilCoords, Cell.Status.OIL);
    generate(shoreCoords, Cell.Status.SHORE);
    generate(vesselCoords, Cell.Status.VESSEL);
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

  private void generate(int[][] coords, Cell.Status status) {
    for (int[] i : coords) {
      generate(i, status);
    }
  }

  private void generate(int[] coords, Cell.Status status) {
    array[coords[1]][coords[0]].setStatus(status);
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

  public boolean isObstacle(int[] coords) {
    Cell.Status status = array[coords[1]][coords[0]].getStatus();
    if (status == Cell.Status.SHORE
        || status == Cell.Status.BARRIER
        || status == Cell.Status.ROCK
        || status == Cell.Status.OILEDSHORE) {
      return true;
    }

    return false;
  }

  public int[] getVesselCoords() {
    return vesselCoords;
  }

  public void moveVessel(int[] oldCoords, int[] newCoords) {
    setGrid(oldCoords[1], oldCoords[0], Cell.Status.CLEAN);
    setGrid(newCoords[1], newCoords[0], Cell.Status.VESSEL);
    this.vesselCoords = newCoords;
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
        switch (array[r][c].getStatus()) {
          case Cell.Status.CLEAN:
            s += String.format(BLUE_BACKGROUND + "%3s" + RESET, array[r][c]);
            break;
          case Cell.Status.OIL:
            s += String.format(BLACK_BACKGROUND + "%3s" + RESET, array[r][c]);
            break;
          case Cell.Status.SHORE:
            s += String.format(GREEN_BACKGROUND + BLACK + "%3s" + RESET, array[r][c]);
            break;
          case Cell.Status.BARRIER:
            s += String.format(RED_BACKGROUND + BLACK + "%3s" + RESET, array[r][c]);
            break;
          case Cell.Status.VESSEL:
            s += String.format(PURPLE_BACKGROUND + "%3s" + RESET, array[r][c]);
            break;
          case Cell.Status.ROCK:
            s += String.format(YELLOW_BACKGROUND + "%3s" + RESET, array[r][c]);
            break;
          case Cell.Status.OILEDSHORE:
            s += String.format("%3s", array[r][c]);
            break;
        }
      }
      s += "\n";
    }

    return s;
  }

  public Cell[][] getGrid() {
    return array;
  }

  public void setGrid(int row, int col, Cell.Status status) {
    array[row][col].setStatus(status);
  }

  public static Direction relativeTurn(Direction initDir, int relDir) {
    int enumLength = Direction.values().length;
    int newDirIdx = (initDir.ordinal() + relDir) % enumLength;
    if (newDirIdx == -1) {
      newDirIdx = enumLength - 1;
    }

    return Direction.values()[newDirIdx];
  }

  public int[] getNeighborCoords(int[] coords, Direction dir) {
    int x = coords[0];
    int y = coords[1];

    switch(dir) {
      case Direction.NORTH:
        y -= 1;
        break;
      case Direction.EAST:
        x += 1;
        break;
      case Direction.SOUTH:
        y += 1;
        break;
      case Direction.WEST:
        x -= 1;
        break;
    }

    // if out of bounds
    if (x < 0 || y < 0 || x >= COLS || y >= ROWS) {
      return null;
    }

    int[] newCoords = {x, y};

    return newCoords;
  }
}
