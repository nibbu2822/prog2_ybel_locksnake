package de.hsbi.lockgame.model;

public final class Position {
  private final int x;
  private final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

    @Override
  public boolean equals(Object o) {
      if (o instanceof Position) {
          if (((Position) o).x == x && ((Position) o).y == y) {
              return true;
          }
      }
      return false;
  }
}
