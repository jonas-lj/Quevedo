package dk.jonaslindstrom.quevedo.moves;

import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.pieces.Piece;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class Move implements
    Function<BidiMap<Position, Piece>, BidiMap<Position, Piece>> {

  private final Piece piece;
  private final Position from;
  private final Position to;

  public Move(Piece piece, Position from, Position to) {
    this.piece = piece;
    this.from = from;
    this.to = to;
  }

  public Piece getPiece() {
    return piece;
  }

  public Position getFrom() {
    return from;
  }

  public Position getTo() {
    return to;
  }

  /** Apply this move to the given board. The map given as a parameter should <b>not</b> be modified. */
  public BidiMap<Position, Piece> apply(BidiMap<Position, Piece> board) {
    BidiMap<Position, Piece> result = new DualHashBidiMap<>(board);

    if (!result.get(from).equals(piece)) {
      throw new IllegalArgumentException("Illegal move");
    }
    result.removeValue(from);
    result.put(getTo(), piece);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Move move = (Move) o;

    if (!Objects.equals(piece, move.piece)) {
      return false;
    }
    if (!Objects.equals(from, move.from)) {
      return false;
    }
    return Objects.equals(to, move.to);
  }

  @Override
  public int hashCode() {
    int result = piece != null ? piece.hashCode() : 0;
    result = 31 * result + (from != null ? from.hashCode() : 0);
    result = 31 * result + (to != null ? to.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return getPiece().getAlgebraicNotation()
        + getFrom()
        + getTo();
  }
}
