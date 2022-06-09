package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.Utils;
import dk.jonaslindstrom.quevedo.moves.Move;
import java.util.List;
import java.util.Map;

public abstract class Piece {

  private final Color color;

  protected Piece(Color color) {
    this.color = color;
  }

  protected static List<Move> untilIllegal(List<Move> moves, State state) {
    int i;
    for (i = 0; i < moves.size(); i++) {
      Move move = moves.get(i);
      if (outsideBoard(move.getTo())) {
        break;
      }
      if (state.get(move.getTo()) != null) {
        if (state.get(move.getTo()).getColor() != move.getPiece().getColor()) {
          i = i + 1;
        }
        break;
      }
    }
    return moves.subList(0, i);
  }

  protected static boolean outsideBoard(Position position) {
    return position.x < 0 || position.x > 7 || position.y < 0 || position.y > 7;
  }

  public Color getColor() {
    return color;
  }

  public abstract int getValue();

  /** Returns a list of moves this piece can make in the current state. It does not consider whether it would leave its king in check */
  public abstract List<Move> legalMoves(Position position, State state);

  public abstract String getAlgebraicNotation();

  public enum Color {
    WHITE, BLACK
  }

  public boolean isThreatened(State state) {
    Map<Piece, Position> opponentsPieces = state.getPieces(Utils.otherColor(color));
    return opponentsPieces.keySet().stream()
        .flatMap(piece -> piece.legalMoves(opponentsPieces.get(piece), state).stream())
        .anyMatch(move -> move.getTo().equals(state.getPosition(this)));
  }

}
