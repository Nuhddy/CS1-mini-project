import java.util.Scanner;

public class Game {
  private Grid grid;
  private int round;
  private int remainingBarriers;
  private int oilCollected;
  private Scanner in;

  private final int[][] INIT_OIL_COORDS = {
    {4, 1}, {5, 1}, {4, 2}, {5, 2}, {6, 2}, {5, 3}, {7, 4}, {8, 4}, {7, 5}, {8, 5}, {9, 5}, {8, 6}
  };
  private final int[][] INIT_SHORE_COORDS = {
    {10, 0}, {11, 0}, {11, 1}, {10, 2}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {10, 6}, {11, 6},
    {0, 7}, {1, 7}, {2, 7}, {9, 7}, {10, 7}, {11, 7}
  };
  private final int[] INIT_VESSEL_COORDS = {0, 4};
  private final int N_ROCKS = 6;

  public Game(int rows, int cols) {
    grid = new Grid(rows, cols, INIT_OIL_COORDS, INIT_SHORE_COORDS, INIT_VESSEL_COORDS, N_ROCKS);
    round = 1;
    remainingBarriers = 5;
    oilCollected = 0;
    in = new Scanner(System.in);
  }

  public Game() {
    this(8, 12);
  }

  private void display() {
    System.out.printf("--- Round %d ---\n", round);
    System.out.print(grid.toString());
    System.out.printf("Current: %s", grid.getCurrent().name().toLowerCase());
    System.out.print("   ");
    System.out.printf("Oil at sea: %s", grid.getOilCoords().length);
    System.out.print("   ");
    System.out.printf("Barriers left: %d", remainingBarriers);
    System.out.print("   ");
    System.out.printf(
        "Vessel at (%d,%d), oil collected: %d",
        grid.getVesselCoords()[0], grid.getVesselCoords()[1], oilCollected);
    System.out.println();
  }

  private void evalPlayerAction() {
    System.out.print("Action (B = place barrier, V = sail vessel): ");
    boolean f = true;
    while (f) {
      switch (in.nextLine().toUpperCase()) {
        case "B":
          placeBarrier();
          f = false;
          break;
        case "V":
          sailVessel();
          f = false;
          break;
        default:
          System.out.println("Please enter B or V.");
          break;
      }
    }
  }

  private void placeBarrier() {
    int[] barrierCoord = new int[2];
    System.out.print("Barrier row: ");
    barrierCoord[0] = in.nextInt();
    in.nextLine();

    System.out.print("Barrier col: ");
    barrierCoord[1] = in.nextInt();
    in.nextLine();
    if (grid.getGrid()[barrierCoord[0]][barrierCoord[1]].getStatus() == Cell.Status.CLEAN) {

      remainingBarriers--;
      grid.setGrid(barrierCoord[0], barrierCoord[1], Cell.Status.BARRIER);
    } else {
      System.out.println("BARRIER can only be placed in CLEAN cell.\nTry again:");
      placeBarrier();
    }
  }

  // TODO: implement
  private void sailVessel() {
    System.out.print("Direction (N, E, S, W): ");
    in.nextLine();

    System.out.print("Number of cells: ");
    in.nextInt();
    in.nextLine();
  }

  public void run() {
    for (int i = 0; i < 99; i++) {
      System.out.println("=== Oil Spill Response ===\n");
      display();
      evalPlayerAction();
    }
  }
}
