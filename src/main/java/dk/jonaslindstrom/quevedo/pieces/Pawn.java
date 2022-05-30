package dk.jonaslindstrom.xerxes.pieces;

import dk.jonaslindstrom.xerxes.State;
import dk.jonaslindstrom.xerxes.Move;
import dk.jonaslindstrom.xerxes.Piece;
import dk.jonaslindstrom.xerxes.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Pawn extends Piece {

  public Pawn(Color color) {
    super(color);
  }

  @Override
  public int getValue() {
    return 1;
  }

  @Override
  public List<Move> legalMoves(Position position, State state) {
    List<Move> result = new ArrayList<>();
    if (getColor() == Color.WHITE) {
      if (state.get(position.add(0,1)) == null) {
        result.add(new Move(this, position, position.add(0, 1)));
        if (hasNotMoved() && state.get(position.add(0, 2)) == null) {
          result.add(new Move(this, position, position.add(0, 2)));
        }
      }

      if (state.get(position.add(1, 1)) != null && state.get(position.add(1, 1)).getColor() == Color.BLACK) {
        result.add(new Move(this, position, position.add(1, 1)));
      }
      if (state.get(position.add(-1, 1)) != null && state.get(position.add(-1, 1)).getColor() == Color.BLACK) {
        result.add(new Move(this, position, position.add(-1, 1)));
      }

    } else {

      if (state.get(position.add(0,-1)) == null) {
        result.add(new Move(this, position, position.add(0, -1)));
        if (hasNotMoved() && state.get(position.add(0, -2)) == null) {
          result.add(new Move(this, position, position.add(0, -2)));
        }
      }

      if (state.get(position.add(1, -1)) != null && state.get(position.add(1, -1)).getColor() == Color.WHITE) {
        result.add(new Move(this, position, position.add(1, -1)));
      }

      if (state.get(position.add(-1, -1)) != null && state.get(position.add(-1, -1)).getColor() == Color.WHITE) {
        result.add(new Move(this, position, position.add(-1, -1)));
      }
    }

    return result.stream().filter(move -> !Piece.outsideBoard(move.getTo())).collect(Collectors.toList());
  }

  @Override
  public String getAlgebraicNotation() {
    return "";
  }

  public String toString() {
    if (getColor() == Color.WHITE) {
      return "♙";
    } else {
      return "♟";
    }
  }

}
