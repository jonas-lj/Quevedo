package dk.jonaslindstrom.quevedo.moves;

import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.pieces.Piece;
import org.apache.commons.collections4.BidiMap;

public class EnPassant extends Move {

  public EnPassant(Piece piece, Position from,
      Position to) {
    super(piece, from, to);
  }

  public BidiMap<Position, Piece> apply(BidiMap<Position, Piece> board) {
    BidiMap<Position, Piece> result = super.apply(board);
    result.remove(new Position(super.getTo().x, super.getFrom().y));
    return result;
  }

  public String toString() {
    return super.toString() + " e.p.";
  }
}
