import java.util.*;

public class Game {
  private Grid grid;
  private int round;
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
    int factorX = 0;
    int factorY = 0;
    switch (dir) {
      case Grid.Direction.NORTH:
        factorY = -1;
        break;
      case Grid.Direction.EAST:
        factorX = 1;
        break;
      case Grid.Direction.SOUTH:
        factorY = 1;
        break;
      case Grid.Direction.WEST:
        factorX = -1;
        break;
    }
    int[] oldCoords = grid.getVesselCoords();
    int[] newCoords = new int[2];

    for (int i = 0; i < dist; i++) {
      newCoords[0] = oldCoords[0] + factorX;
      newCoords[1] = oldCoords[1] + factorY;
      try {
        if (grid.isObstacle(newCoords)) {
          Cell.Status cand = grid.getGrid()[newCoords[1]][newCoords[0]].getStatus();
          System.out.printf(
              "Sailed %d of %d field(s) to avoid %s.\n", i, dist, cand.name().toLowerCase());
          break;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        System.out.printf("Sailed %d of %d field(s) to stay within bounds.\n", i, dist);
        break;
      }
      grid.setGrid(oldCoords[1], oldCoords[0], Cell.Status.CLEAN);
      grid.setVessel(newCoords);
      oldCoords = Arrays.copyOf(newCoords, 2);
    }

    // skim oil
    int[] vessel = grid.getVesselCoords();
    int[][] cands = new int[4][2];
    int oilSkimmed = 0;

    cands[0][0] = vessel[0] - 1;
    cands[0][1] = vessel[1];

    cands[1][0] = vessel[0] + 1;
    cands[1][1] = vessel[1];

    cands[2][0] = vessel[0];
    cands[2][1] = vessel[1] - 1;

    cands[3][0] = vessel[0];
    cands[3][1] = vessel[1] + 1;

    for (int[] i : cands) {
      try {
        if (grid.getGrid()[i[1]][i[0]].getStatus() == Cell.Status.OIL) {
          grid.setGrid(i[1], i[0], Cell.Status.CLEAN);
          oilSkimmed++;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
      }
    }

    collectedOil += oilSkimmed;
    remainingOil -= oilSkimmed;

    System.out.printf(
        "The vessel is at (%d,%d) and skimmed %d oil cell(s).\n", vessel[0], vessel[1], oilSkimmed);
  }

  private Grid.Direction windDir(Grid.Direction dir) {
    Random random = new Random();
    double s = random.nextDouble();
    Grid.Direction newDir = Grid.Direction.NORTH;
    if (s < 0.5) {
      newDir = dir;
    } else if (0.5 > s && s < 0.75) {
      newDir = Grid.turnRight(dir);
    } else if (s > 0.75) {
      newDir = Grid.turnLeft(dir);
    }
    return newDir;
  }

  public void run() {
    for (int i = 0; i < 99; i++) {
      System.out.println("=== Oil Spill Response ===\n");
      display();
      evalPlayerAction();
    }
  }
}
