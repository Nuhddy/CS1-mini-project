/*
 * Authors: N. Elkhatali 83938
 * S. Stefanovich 83898
 *
 */
/** Code for Cell object containing a simple status */
public class Cell {
  public enum Status {
    CLEAN,
    OIL,
    SHORE,
    BARRIER,
    VESSEL,
    ROCK,
    OILEDSHORE,
  }

  private Status status;

  public Cell(Status status) {
    this.status = status;
  }

  public Cell() {
    this(Status.CLEAN);
  }

  /** Retrieves Status enum */
  public Status getStatus() {
    return this.status;
  }

  /** Changes Status enum */
  public void setStatus(Status status) {
    this.status = status;
  }

  /** Checks if two Cell objects have the same Status */
  public boolean equals(Cell cell) {
    if (cell.getStatus() == this.status) {
      return true;
    }

    return false;
  }

  /** Converts Cell object into a string */
  @Override
  public String toString() {
    String s = "";
    switch (status) {
      case Status.CLEAN:
        s = "~";
        break;
      case Status.OIL:
        s = "O";
        break;
      case Status.SHORE:
        s = "S";
        break;
      case Status.BARRIER:
        s = "#";
        break;
      case Status.VESSEL:
        s = "V";
        break;
      case Status.ROCK:
        s = "R";
        break;
      case Status.OILEDSHORE:
        s = "X";
        break;
    }
    return s;
  }
}
