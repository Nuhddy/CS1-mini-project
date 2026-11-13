public class Cell {
  public enum Status {
    CLEAN,
    OIL,
    SHORE,
    BARRIER,
    VESSEL,
    ROCK,
    OILEDSHORE
  }

  private Status status;

  public Cell(Status status) {
    this.status = status;
  }

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

  public Cell.Status getStatus() {
    return this.status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public boolean equals(Cell cell) {
    if (cell.getStatus() == this.status) {
      return true;
    } else {
      return false;
    }
  }
}
