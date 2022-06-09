package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class King extends Piece {

  public King(Color color) {
    super(color);
  }

  @Override
  public int getValue() {
    return Integer.MAX_VALUE;
  }

  @Override
  public List<Move> legalMoves(Position position, State state) {
    List<Move> result = Stream.of(
        position.add(0, -1),
        position.add(0, 1),
        position.add(-1, -1),
        position.add(-1, 0),
        position.add(-1, 1),
        position.add(1, -1),
        position.add(1, 0),
        position.add(1, 1))
        .filter(p -> !Piece.outsideBoard(p))
        .filter(p -> state.get(p) == null || state.get(p).getColor() != this.getColor())
        .map(p -> new Move(this, position, p))
        .collect(Collectors.toCollection(ArrayList::new));

    return result;
  }

  @Override
  public String getAlgebraicNotation() {
    return "K";
  }

  public String toString() {
    if (getColor() == Color.WHITE) {
      return "♔";
    } else {
      return "♚";
    }
  }

}
