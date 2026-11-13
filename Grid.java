public class Grid {
  private int rows;
  private int cols;
  private Cell[][] array;
  private final int[][] OILCOORDS = {
    {4, 1}, {5, 1}, {4, 2}, {5, 2}, {6, 2}, {5, 3}, {7, 4}, {8, 4}, {7, 5}, {8, 5}, {9, 5}, {8, 6}
  };
  private final int[][] SHORECOORDS = {
    {10, 0}, {11, 0}, {11, 1}, {10, 2}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {10, 6}, {11, 6},
    {0, 7}, {1, 7}, {2, 7}, {9, 7}, {10, 7}, {11, 7}
  };

  public Grid(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    array = generateClean(rows, cols);
    generate(OILCOORDS, Cell.Status.OIL);
    generate(SHORECOORDS, Cell.Status.SHORE);
  }

  private Cell[][] generateClean(int rows, int cols) {
    Cell[][] array = new Cell[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        array[r][c] = new Cell(Cell.Status.CLEAN);
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

  public void update() {}

  public String toString() {
    String print = "";
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        print += array[r][c] + " ";
      }
      print += "\n";
    }
    return print;
  }

  public static void main(String[] args) {}
}
