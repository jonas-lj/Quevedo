package dk.jonaslindstrom.xerxes;

public class LoggedMove {

  private final String representation;

  public LoggedMove(Move move, boolean capture, boolean check) {
    if (move instanceof Castling) {
      Castling castling = ((Castling) move);
      this.representation = castling.isKingSide() ? "0-0" : "0-0-0";
    } else {
      this.representation =
          move.getPiece().getAlgebraicNotation()
              + move.getFrom()
              + (capture ? "x" : "")
              + move.getTo()
              + (check ? "+" : "");
    }
  }

  public String toString() {
    return representation;
  }

}
