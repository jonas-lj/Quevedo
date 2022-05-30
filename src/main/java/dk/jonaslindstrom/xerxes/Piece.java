package dk.jonaslindstrom.xerxes;

import java.util.List;

public abstract class Piece {

  private final Color color;
  private boolean hasMoved;

  protected Piece(Color color) {
    this.color = color;
  }

  public boolean hasNotMoved() {
    return !hasMoved;
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

  public abstract List<Move> legalMoves(Position position, State state);

  public void move(Move move) {
    hasMoved = true;
  }

  public abstract String getAlgebraicNotation();

  public enum Color {
    WHITE, BLACK
  }

}
