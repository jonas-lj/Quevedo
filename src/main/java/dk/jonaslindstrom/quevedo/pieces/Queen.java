package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Queen extends Piece {

  public Queen(Color color) {
    super(color);
  }

  @Override
  public int getValue() {
    return 8;
  }

  @Override
  public List<Move> legalMoves(Position position, State state) {
    List<Move> result = new ArrayList<>();
    result.addAll(Piece.untilIllegal(IntStream.rangeClosed(1, 7)
        .mapToObj(i -> new Move(this, position, position.add(i, i))).collect(
            Collectors.toList()), state));
    result.addAll(Piece.untilIllegal(IntStream.rangeClosed(1, 7)
        .mapToObj(i -> new Move(this, position, position.add(-i, -i))).collect(
            Collectors.toList()), state));
    result.addAll(Piece.untilIllegal(IntStream.rangeClosed(1, 7)
        .mapToObj(i -> new Move(this, position, position.add(i, -i))).collect(
            Collectors.toList()), state));
    result.addAll(Piece.untilIllegal(IntStream.rangeClosed(1, 7)
        .mapToObj(i -> new Move(this, position, position.add(-i, i))).collect(
            Collectors.toList()), state));
    result.addAll(Piece.untilIllegal(IntStream.rangeClosed(1, 7)
        .mapToObj(i -> new Move(this, position, position.add(i, 0))).collect(
            Collectors.toList()), state));
    result.addAll(Piece.untilIllegal(IntStream.rangeClosed(1, 7)
        .mapToObj(i -> new Move(this, position, position.add(-i, 0))).collect(
            Collectors.toList()), state));
    result.addAll(Piece.untilIllegal(IntStream.rangeClosed(1, 7)
        .mapToObj(i -> new Move(this, position, position.add(0, i))).collect(
            Collectors.toList()), state));
    result.addAll(Piece.untilIllegal(IntStream.rangeClosed(1, 7)
        .mapToObj(i -> new Move(this, position, position.add(0, -i))).collect(
            Collectors.toList()), state));
    return result;
  }

  @Override
  public String getAlgebraicNotation() {
    return "Q";
  }

  public String toString() {
    if (getColor() == Color.WHITE) {
      return "♕";
    } else {
      return "♛";
    }
  }

}
