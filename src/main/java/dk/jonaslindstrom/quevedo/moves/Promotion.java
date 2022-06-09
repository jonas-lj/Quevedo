package dk.jonaslindstrom.quevedo.moves;

import dk.jonaslindstrom.quevedo.pieces.Piece;
import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.pieces.Bishop;
import dk.jonaslindstrom.quevedo.pieces.Knight;
import dk.jonaslindstrom.quevedo.pieces.Queen;
import dk.jonaslindstrom.quevedo.pieces.Rook;
import java.util.List;
import org.apache.commons.collections4.BidiMap;

public class Promotion extends Move {

  public String getType() {
    return type;
  }

  private final String type;

  public Promotion(Piece piece, Position from, Position to, String type) {
    super(piece, from, to);
    this.type = type;
  }

  public BidiMap<Position, Piece> apply(BidiMap<Position, Piece> board) {
    BidiMap<Position, Piece> newBoard = super.apply(board);

    switch (type) {
      case "Q":
        newBoard.put(getTo(), new Queen(getPiece().getColor()));
        break;
      case "R":
        newBoard.put(getTo(), new Rook(getPiece().getColor()));
        break;
      case "B":
        newBoard.put(getTo(), new Bishop(getPiece().getColor()));
        break;
      case "N":
        newBoard.put(getTo(), new Knight(getPiece().getColor()));
        break;
      default:
        throw new IllegalArgumentException("Unknown promotion piece type: " + type);
    }
    return newBoard;
  }

  public static List<Move> allPromotions(Piece piece, Position from, Position to) {
    return List.of(
        new Promotion(piece, from, to, "Q"),
        new Promotion(piece, from, to, "R"),
        new Promotion(piece, from, to, "B"),
        new Promotion(piece, from, to, "N"));
  }

  @Override
  public String toString() {
    return super.toString() + "=" + type;
  }


}
