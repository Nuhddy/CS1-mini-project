/*
 * Authors:
 * N. Elkhatali, 83938
 * S. Stefanovich, 83898
 *
 */

/**
  * Represents a cell containing its status.
  */
public class Cell {

  /**
    * Enumerator containing values for valid cell status.
    */
  public enum Status {
    CLEAN,
    OIL,
    SHORE,
    BARRIER,
    VESSEL,
    ROCK,
    OILEDSHORE,
  }

  // Mutable field
  private Status status;

  /**
    * Main constructor.
    * @param status Status of cell.
    */
  public Cell(Status status) {
    this.status = status;
  }

  /**
    * Overloaded constructor method.
    * Calls main constructor with CLEAN status.
    */
  public Cell() {
    this(Status.CLEAN);
  }

  /**
    * Get status.
    */
  public Status getStatus() {
    return this.status;
  }

  /**
    * Set status.
    */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
    * Check if status is equal to that of another Cell object.
    */
  public boolean equals(Cell cell) {
    if (cell.getStatus() == this.status) {
      return true;
    }

    return false;
  }

  /**
    * Represents a Cell object as a string based on its status.
    */
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
