package dk.jonaslindstrom.quevedo;

import dk.jonaslindstrom.quevedo.pieces.Piece;
import dk.jonaslindstrom.quevedo.pieces.Piece.Color;

public class Utils {

  public static Color otherColor(Color color) {
    return color == Color.WHITE ? Color.BLACK : Color.WHITE;
  }

  public static Piece getKing(Color color, State state) {
    return state.getPieces("K", color).keySet().stream().findAny().orElseThrow();
  }
}
