public class OilSpillResponse {
  public static void main(String[] args) {
    Grid grid = new Grid(8, 12);
    System.out.println(grid);

    Cell cell = new Cell(Cell.Status.OIL);
    System.out.println(cell);
  }
}
