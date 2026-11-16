import java.util.*;

public class Game {
  private Grid grid;

  private int round;
  private Grid.Direction current;
  private int remainingBarriers;
  private int remainingOil;
  private int collectedOil;

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
    current = Grid.Direction.EAST;
    remainingBarriers = 5;
    remainingOil = INIT_OIL_COORDS.length;
    collectedOil = 0;

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
    System.out.printf("Oil at sea: %s", remainingOil);
    System.out.print("   ");
    System.out.printf("Barriers left: %d", remainingBarriers);
    System.out.print("   ");
    System.out.printf(
      "Vessel at (%d,%d), oil collected: %d",
      grid.getVesselCoords()[0], grid.getVesselCoords()[1], collectedOil);
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

  private void sailVessel() {
    Grid.Direction dir = null;
    int dist = 0;
    int collectedOil = 0;

    // choose direction
    boolean f = true;
    while (f) {
      System.out.print("Direction (N, E, S, W): ");
      switch (in.nextLine().toUpperCase()) {
        case "N":
          dir = Grid.Direction.NORTH;
          f = false;
          break;
        case "E":
          dir = Grid.Direction.EAST;
          f = false;
          break;
        case "S":
          dir = Grid.Direction.SOUTH;
          f = false;
          break;
        case "W":
          dir = Grid.Direction.WEST;
          f = false;
          break;
        default:
          System.out.println("Please enter N, E, S, or W.");
          break;
      }
    }

    // choose distance
    f = true;
    while (f) {
      System.out.print("Number of cells: ");
      if (in.hasNextInt()) {
        int i = in.nextInt();
        if (i >= 1 && i <= 4) {
          dist = i;
          f = false;
          in.nextLine();
        } else {
          System.out.println("Please enter a number between 1-4.");
        }
      } else {
        in.nextLine();
      }
    }

    // move vessel
    int[] vessel = grid.getVesselCoords();

    for (int i = 0; i < dist; i++) {
      int[] cand = grid.getNeighborCoords(vessel, dir);

      // if candidate is out of bounds
      if (cand == null) {
        System.out.printf("Sailed %d of %d field(s) to stay within bounds.\n", i, dist);
        break;
      }

      // if candidate is obstacle
      if (grid.isObstacle(cand)) {
        Cell.Status status = grid.getGrid()[cand[1]][cand[0]].getStatus();
        System.out.printf(
          "Sailed %d of %d field(s) to avoid %s.\n", i, dist, status.name().toLowerCase()
        );
        break;
      }

      // if candidate is oil
      if (grid.getGrid()[cand[1]][cand[0]].getStatus() == Cell.Status.OIL) {
        collectedOil++;
      }

      // update state
      grid.moveVessel(vessel, cand);
      vessel = Arrays.copyOf(cand, 2);
    }

    // skim oil
    vessel = grid.getVesselCoords();
    Grid.Direction[] dirs = Grid.Direction.values();

    for (int i = 0; i < dirs.length; i++) {
      int[] cand = grid.getNeighborCoords(vessel, dirs[i]);

      // if candidate is out of bounds
      if (cand == null) {
        continue;
      }

      // if candidate is oil
      if (grid.getGrid()[cand[1]][cand[0]].getStatus() == Cell.Status.OIL) {
        // update state
        grid.setGrid(cand[1], cand[0], Cell.Status.CLEAN);
        collectedOil++;
      }
    }

    this.collectedOil += collectedOil;
    this.remainingOil -= collectedOil;
    System.out.printf(
      "The vessel is at (%d,%d) and skimmed %d oil cell(s).\n", vessel[0], vessel[1], collectedOil
    );
  }

  // TODO: implement
  private void updateOilCells() {
    // loop over all cells in grid
      // check if cell is oil
        // check if neighbor is out of bounds
        // check if neighbor is obstacle
        // check if neighbor is clean
          // move original oil cell to new cell
  }

  private Grid.Direction updateCurrent(Grid.Direction dir) {
    Random r = new Random();
    int i = r.nextInt(4);

    if (i == 0) {
      return Grid.relativeTurn(dir, -1);
    } else if (i == 1) {
      return Grid.relativeTurn(dir, 1);
    }

    return dir;
  }

  public void run() {
    while (remainingOil > 0) {
      // current round
      System.out.println("=== Oil Spill Response ===\n");
      display();
      evalPlayerAction();

      // set up next round
      updateOilCells();
      round++;
      current = updateCurrent(current);
    }

    // game over
    display();
  }
}
