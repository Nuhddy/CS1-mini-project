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
    return status.toString();
  }
}
