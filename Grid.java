public class Grid {
  private int rows;
  private int cols;
  private Cell[][] array;

  public Grid(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    array = new Cell[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        array[r][c] = new Cell(Cell.Status.OIL);
      }
    }
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
