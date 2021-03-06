package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.Position;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Knight extends Piece {

  public Knight(Color color) {
    super(color);
  }

  @Override
  public int getValue() {
    return 3;
  }

  @Override
  public List<Move> legalMoves(Position position, State state) {
    List<Move> result = Stream.of(
        position.add(2, 1),
        position.add(2, -1),
        position.add(-2, 1),
        position.add(-2, -1),
        position.add(1, 2),
        position.add(1, -2),
        position.add(-1, 2),
        position.add(-1, -2))
        .filter(p -> !Piece.outsideBoard(p))
        .filter(p -> state.get(p) == null || state.get(p).getColor() != this.getColor())
        .map(p -> new Move(this, position, p))
        .collect(Collectors.toList());
    return result;
  }

  @Override
  public String getAlgebraicNotation() {
    return "N";
  }

  public String toString() {
    if (getColor() == Color.WHITE) {
      return "♘";
    } else {
      return "♞";
    }
  }

}
