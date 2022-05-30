package dk.jonaslindstrom.xerxes;

import dk.jonaslindstrom.xerxes.Piece.Color;
import org.apache.commons.collections4.BidiMap;

public class Castling extends Move {

  private final boolean kingSide;

  public Castling(Piece rook, boolean kingSide) {
    super(rook, getFrom(rook.getColor(), kingSide), getTo(rook.getColor(), kingSide));
    this.kingSide = kingSide;
  }

  public boolean isKingSide() {
    return kingSide;
  }

  private static Position getFrom(Color color, boolean kingSide) {
    int y = color == Color.WHITE ? 0 : 7;
    int x = kingSide ? 7 : 0;
    return new Position(x, y);
  }

  private static Position getTo(Color color, boolean kingSide) {
    int y = color == Color.WHITE ? 0 : 7;
    int x = kingSide ? 5 : 3;
    return new Position(x, y);
  }

  public BidiMap<Position, Piece> apply(BidiMap<Position, Piece> board) {
      Move king = new Move(board.get(new Position(4, getFrom().y)), new Position(4, getFrom().y),
          new Position(4 + (kingSide ? 2 : -2), getFrom().y));
      return king.apply(super.apply(board));
  }

  @Override
  public String toString() {
    return "Castling: " + super.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    Castling castling = (Castling) o;

    return kingSide == castling.kingSide;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (kingSide ? 1 : 0);
    return result;
  }
}
