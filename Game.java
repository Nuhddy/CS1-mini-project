public class Game {
  private Grid grid;
  private int round;

  public Game() {
    grid = new Grid();
    round = 1;
  }

  private void display() {
    System.out.printf("--- Round %d ---\n", round);
    System.out.print(grid.toString());
    System.out.printf("Current: %s", grid.getCurrent().name().toLowerCase());
    System.out.print("   ");
    System.out.printf("Oil at sea: -");
    System.out.print("   ");
    System.out.printf("Barriers left: -");
    System.out.print("   ");
    System.out.printf("Vessel at: -, oil collected: -");
    System.out.println();
  }

  public void run() {
    System.out.println("=== Oil Spill Response ===\n");
    display();
  }
}
