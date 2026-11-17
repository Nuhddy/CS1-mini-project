/*
 * Authors:
 * N. Elkhatali, 83938
 * S. Stefanovich, 83898
 */

import java.util.*;

/** Represents game. Contains game logic that decides behavior of grid and cell. */
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
    System.out.printf("Current: %s", current.name().toLowerCase());
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

  /** Places a barrier at a given coordinate on the grid */
  private void placeBarrier() {
    int[] barrierCoord = new int[2];
    boolean f = true;
    boolean g = true;
    int row = 0;
    int col = 0;

    while (f) {
      System.out.println("Barrier row: ");
      if (in.hasNextInt()) { // validate as int
        row = in.nextInt();
        if (row < grid.getGrid().length && row >= 0) { // validate range
          barrierCoord[0] = row;
          f = false;
          in.nextLine();
        } else {
          System.err.println(
              "Please choose an appropiate integer: 0-" + (grid.getGrid().length - 1));
        }
      } else {
        in.nextLine();
      }
    }
    while (g) {
      System.out.println("Barrier col: ");
      if (in.hasNextInt()) { // validate as int
        col = in.nextInt();
        if (col < grid.getGrid()[0].length && row >= 0) { // validate range
          barrierCoord[1] = col;
          g = false;
          in.nextLine();
        } else {
          System.err.println(
              "Please choose an appropiate integer 0-" + (grid.getGrid()[0].length - 1));
        }
      } else {
        in.nextLine();
      }
    }

    if (grid.getGrid()[barrierCoord[0]][barrierCoord[1]].getStatus()
        == Cell.Status.CLEAN) { // check if coords given match a CLEAN cell.
      remainingBarriers--;
      grid.setGrid(barrierCoord[0], barrierCoord[1], Cell.Status.BARRIER);
    } else {
      System.err.println("BARRIER can only be placed in CLEAN cell.\nTry again:");
      placeBarrier();
    }
  }

  private int moveVessel(Grid.Direction dir, int dist) {
    int[] vessel = grid.getVesselCoords();
    int collectedOil = 0;

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
            "Sailed %d of %d field(s) to avoid %s.\n", i, dist, status.name().toLowerCase());
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

    return collectedOil;
  }

  private int skimOil() {
    int[] vessel = grid.getVesselCoords();
    int collectedOil = 0;

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

    return collectedOil;
  }

  private void sailVessel() {
    // choose direction
    Grid.Direction dir = null;
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
    int dist = 0;
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

    int collectedOil = 0;
    collectedOil += moveVessel(dir, dist);
    collectedOil += skimOil();

    this.collectedOil += collectedOil;
    this.remainingOil -= collectedOil;

    int[] vessel = grid.getVesselCoords();
    System.out.printf(
        "The vessel is at (%d,%d) and skimmed %d oil cell(s).\n",
        vessel[0], vessel[1], collectedOil);
  }

  /** Moves appropiate OIL cells along the Grid.Direction given in grid object. */
  private void updateOilCells() {
    int[] oldCoords = new int[2];
    int[] newCoords = new int[2];
    int[][] oilCoords = new int[remainingOil][2];
    int[][] cleanCoords = new int[remainingOil][2];
    int[][] oiledShoords = new int[remainingOil][2];
    int checkers = 0;
    int chess = 0;

    // loop over all cells in grid
    for (int i = 0; i < grid.getGrid().length; i++) {
      for (int j = 0; j < grid.getGrid()[0].length; j++) {
        oldCoords[0] = j;
        oldCoords[1] = i;
        if (grid.getGrid()[oldCoords[1]][oldCoords[0]].getStatus()
            == Cell.Status.OIL) { // check if cell is oil.

          newCoords =
              grid.getNeighborCoords(oldCoords, current); // get neighbor in CURRENT direction
          if (newCoords == null) {
            System.out.println("Null @ " + j + " " + i);
            continue;
          }
          if (grid.getGrid()[newCoords[1]][newCoords[0]].getStatus() == Cell.Status.SHORE) {
            oiledShoords[chess] =
                Arrays.copyOf(newCoords, 2); // there is a more efficient way of doing this.
            chess++;
            remainingOil--;
          }

          if (grid.getGrid()[newCoords[1]][newCoords[0]].getStatus() == Cell.Status.CLEAN) {
            System.out.println("Clean @ " + j + " " + i);

            cleanCoords[checkers] = Arrays.copyOf(oldCoords, 2); // store value in cleanCoords
            oilCoords[checkers] = Arrays.copyOf(newCoords, 2); // store value in oilCoords
            checkers++;
          }
        }
      }
    }
    // #TODO: This snippet should just reuse generate from Grid class
    for (int i = 0; i < checkers; i++) { // loops over board placing OIL and CLEAN cells.
      grid.setGrid(cleanCoords[i][1], cleanCoords[i][0], Cell.Status.CLEAN);
      grid.setGrid(oilCoords[i][1], oilCoords[i][0], Cell.Status.OIL);
    }
    for (int i = 0; i < chess; i++) { // loops over board placing OILEDSHORE cells.
      grid.setGrid(oiledShoords[i][1], oiledShoords[i][0], Cell.Status.OILEDSHORE);
    }
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
      this.current = updateCurrent(current);
    }

    // game over
    display();
  }
}
