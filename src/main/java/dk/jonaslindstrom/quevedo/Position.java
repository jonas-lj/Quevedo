package dk.jonaslindstrom.quevedo;

public class Position {

  private static final String HORIZONTAL = "abcdefgh";
  public final int x;
  public final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position add(int dx, int dy) {
    return new Position(x + dx, y + dy);
  }

  public String toString() {
    return HORIZONTAL.substring(x, x+1) + (y + 1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Position position = (Position) o;

    if (x != position.x) {
      return false;
    }
    return y == position.y;
  }

  @Override
  public int hashCode() {
    int result = x;
    result = 31 * result + y;
    return result;
  }
}
