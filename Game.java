/*
 * Authors:
 * N. Elkhatali, 83938
 * S. Stefanovich, 83898
 */

import java.util.*;

/**
  * Represents game. Contains game logic that decides behavior of grid and cells.
  */
public class Game {

  // Mutable fields for game state.
  private Grid grid;
  private int round;
  private Grid.Direction current;
  private int remainingBarriers;
  private int remainingOil;
  private int collectedOil;

  // User input scanner.
  private Scanner in;

  // Constants: initial coordinates of oil, shore, and vessel and amount of rocks to place.
  private final int[][] INIT_OIL_COORDS = {
    {4, 1}, {5, 1}, {4, 2}, {5, 2}, {6, 2}, {5, 3}, {7, 4}, {8, 4}, {7, 5}, {8, 5}, {9, 5}, {8, 6}
  };
  private final int[][] INIT_SHORE_COORDS = {
    {10, 0}, {11, 0}, {11, 1}, {10, 2}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {10, 6}, {11, 6},
    {0, 7}, {1, 7}, {2, 7}, {9, 7}, {10, 7}, {11, 7}
  };
  private final int[] INIT_VESSEL_COORDS = {0, 4};
  private final int N_ROCKS = 6;

  /**
    * Main constructor for a Game object.
    * @param rows amount of rows.
    * @param cols amount of cols.
    */
  public Game(int rows, int cols) {
    grid = new Grid(rows, cols, INIT_OIL_COORDS, INIT_SHORE_COORDS, INIT_VESSEL_COORDS, N_ROCKS);

    round = 1;
    current = Grid.Direction.EAST;
    remainingBarriers = 5;
    remainingOil = INIT_OIL_COORDS.length;
    collectedOil = 0;

    in = new Scanner(System.in);
  }

  /**
    * Overloaded constructor method.
    * Calls main constructor with grid dimensions 8x12.
    */
  public Game() {
    this(8, 12);
  }

  /**
    * Display game state info.
    */
  private void displayRound() {
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

  /**
    * Display results.
    */
  private void displayGameOver() {
    System.out.println("=== Game over: no more oil at sea ===");
    System.out.print(grid.toString());
    System.out.printf("Oil recovered: %d of %d", collectedOil, INIT_OIL_COORDS.length);
    System.out.print("   ");
    System.out.printf("Oil washed ashore: %d", INIT_OIL_COORDS.length - collectedOil);
    System.out.println();
    System.out.printf("Score: %d\n", collectedOil);
  }

  /**
    * Entry point for evaluating player action.
    */
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

  /**
    * Places a barrier at given coordinates on the grid.
    */
  private void placeBarrier() {
    int[] barrierCoord = new int[2];
    boolean f = true;
    boolean g = true;
    int row = 0;
    int col = 0;

    while (f) {
      System.out.print("Barrier row: ");
      if (in.hasNextInt()) { // validate as int
        row = in.nextInt();
        if (row < grid.getGrid().length && row >= 0) { // validate range
          barrierCoord[1] = row;
          f = false;
          in.nextLine();
        } else {
          System.err.printf(
            "Please choose an appropiate integer: 0-%d.\n", grid.getGrid().length - 1);
        }
      } else {
        in.nextLine();
      }
    }
    while (g) {
      System.out.print("Barrier col: ");
      if (in.hasNextInt()) { // validate as int
        col = in.nextInt();
        if (col < grid.getGrid()[0].length && col >= 0) { // validate range
          barrierCoord[0] = col;
          g = false;
          in.nextLine();
        } else {
          System.err.printf(
            "Please choose an appropiate integer 0-%d.\n", grid.getGrid()[0].length - 1);
        }
      } else {
        in.nextLine();
      }
    }

    if (grid.getCell(barrierCoord[0], barrierCoord[1]).getStatus()
        == Cell.Status.CLEAN) { // check if coords given match a CLEAN cell.
      remainingBarriers--;
      grid.setCell(barrierCoord[0], barrierCoord[1], Cell.Status.BARRIER);
      System.out.printf("Barrier placed at (%d,%d).\n", barrierCoord[0], barrierCoord[1]);
    } else {
      System.err.println("Barriers can only be placed on clean water, away from the vessel.");
      placeBarrier();
    }
  }

  /**
    * Handles user input for direction and distance of vessel movement.
    */
  private void sailVessel() {
    // Choose direction.
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

    // Choose distance.
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

    // Advance simulation.
    int collectedOil = 0;
    collectedOil += moveVessel(dir, dist);
    collectedOil += skimOil();

    // Update game state.
    this.collectedOil += collectedOil;
    this.remainingOil -= collectedOil;

    int[] vessel = grid.getVesselCoords();
    System.out.printf(
      "The vessel is at (%d,%d) and skimmed %d oil cell(s).\n",
      vessel[0], vessel[1], collectedOil);
  }

  /**
    * Handles movement of vessel according to user input, obstacles, and grid boundaries.
    */
  private int moveVessel(Grid.Direction dir, int dist) {
    int[] vessel = grid.getVesselCoords();
    int collectedOil = 0;

    for (int i = 0; i < dist; i++) {
      int[] cand = grid.getNeighborCoords(vessel, dir);

      // Check if candidate is out of bounds.
      if (cand == null) {
        System.out.printf("Sailed %d of %d field(s) to stay within bounds.\n", i, dist);
        break;
      }

      // Check if candidate is obstacle.
      if (grid.isObstacle(cand)) {
        Cell.Status status = grid.getCell(cand[0], cand[1]).getStatus();
        System.out.printf(
          "Sailed %d of %d field(s) to avoid %s.\n", i, dist, status.name().toLowerCase());
        break;
      }

      // Check if candidate is oil.
      if (grid.getCell(cand[0], cand[1]).getStatus() == Cell.Status.OIL) {
        collectedOil++;
      }

      // Update game state.
      grid.moveVessel(vessel, cand);
      vessel = Arrays.copyOf(cand, 2);
    }

    return collectedOil;
  }

  /**
    * Collects oil cells surrounding vessel.
    */
  private int skimOil() {
    int[] vessel = grid.getVesselCoords();
    int collectedOil = 0;

    // Loop over each direction.
    Grid.Direction[] dirs = Grid.Direction.values();
    for (int i = 0; i < dirs.length; i++) {
      int[] cand = grid.getNeighborCoords(vessel, dirs[i]);

      // Check if candidate is out of bounds.
      if (cand == null) {
        continue;
      }

      // Check if candidate is oil.
      if (grid.getCell(cand[0], cand[1]).getStatus() == Cell.Status.OIL) {
        // Update game state.
        grid.setCell(cand[0], cand[1], Cell.Status.CLEAN);
        collectedOil++;
      }
    }

    return collectedOil;
  }

  /**
    * Moves appropiate OIL cells along the Grid.Direction given in grid object.
    */
  private void updateOilCells() {
    // Temporary arrays.
    int[] oldCoords = new int[2];
    int[] newCoords = new int[2];

    // Arrays for keeping tracking coordinates of specific Cell Status.
    int[][] oilCoords = new int[remainingOil][2];
    int[][] cleanOilCoords = new int[remainingOil][2];
    int[][] oiledShoreCoords = new int[remainingOil][2];
    int[][] cleanOiledShoreCoords = new int[remainingOil][2];

    // Counters for array indeces.
    int oilCounter = 0;
    int oiledShoreCounter = 0;

    // Loop over all cells in grid.
    for (int i = 0; i < grid.getGrid().length; i++) {
      for (int j = 0; j < grid.getGrid()[0].length; j++) {
        oldCoords[0] = j;
        oldCoords[1] = i;
        // Check if cell is oil.
        if (grid.getCell(oldCoords[0], oldCoords[1]).getStatus() == Cell.Status.OIL) {
          // Get neighbor in direction of current.
          newCoords = grid.getNeighborCoords(oldCoords, current);
          if (newCoords == null) {
            continue;
          }
          // Store coordinates of where oiled shore cells should move to.
          if (grid.getCell(newCoords[0], newCoords[1]).getStatus() == Cell.Status.SHORE) {
            cleanOiledShoreCoords[oiledShoreCounter] = Arrays.copyOf(oldCoords, 2);
            oiledShoreCoords[oiledShoreCounter] = Arrays.copyOf(newCoords, 2);
            oiledShoreCounter++;

            remainingOil--;
          }

          // Store coordinates of where oil cells should move to.
          if (grid.getCell(newCoords[0], newCoords[1]).getStatus() == Cell.Status.CLEAN) {
            cleanOilCoords[oilCounter] = Arrays.copyOf(oldCoords, 2);
            oilCoords[oilCounter] = Arrays.copyOf(newCoords, 2);
            oilCounter++;
          }
        }
      }
    }

    // Update game state.
    // Places new oil cells and replace old oil cells with clean cells.
    for (int i = 0; i < oilCounter; i++) {
      grid.setCell(cleanOilCoords[i][0], cleanOilCoords[i][1], Cell.Status.CLEAN);
      grid.setCell(oilCoords[i][0], oilCoords[i][1], Cell.Status.OIL);
    }
    // Places new oiled shore cells and replace old oil cells with clean cells.
    for (int i = 0; i < oiledShoreCounter; i++) {
      grid.setCell(cleanOiledShoreCoords[i][0], cleanOiledShoreCoords[i][1], Cell.Status.CLEAN);
      grid.setCell(oiledShoreCoords[i][0], oiledShoreCoords[i][1], Cell.Status.OILEDSHORE);
    }
  }

  /**
    * Update direction of the current.
    */
  private void updateCurrent() {
    Random r = new Random();
    Grid.Direction newDir = current;
    int i = r.nextInt(4);

    if (i == 0) {
      newDir = Grid.relativeTurn(current, -1);
    } else if (i == 1) {
      newDir = Grid.relativeTurn(current, 1);
    }

    current = newDir;
  }

  /**
    * Main game loop.
    */
  public void run() {
    // Start of game.
    System.out.println("=== Oil Spill Response ===\n");

    // Loop until game is won.
    while (remainingOil > 0) {
      // Evaluate current round.
      displayRound();
      evalPlayerAction();

      // Set up next round.
      updateOilCells();
      round++;
      updateCurrent();
      System.out.println();
    }

    // Game over.
    displayGameOver();
  }
}
