package dk.jonaslindstrom.quevedo.moves;

import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.pieces.*;
import org.apache.commons.collections4.BidiMap;

import java.util.List;

public class Promotion extends Move {

    private final String type;

    public Promotion(Piece piece, Position from, Position to, String type) {
        super(piece, from, to);
        this.type = type;
    }

    public static List<Move> allPromotions(Piece piece, Position from, Position to) {
        return List.of(
                new Promotion(piece, from, to, "Q"),
                new Promotion(piece, from, to, "R"),
                new Promotion(piece, from, to, "B"),
                new Promotion(piece, from, to, "N"));
    }

    public String getType() {
        return type;
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

    @Override
    public String toString() {
        return super.toString() + "=" + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Promotion promotion = (Promotion) o;

        return type != null ? type.equals(promotion.type) : promotion.type == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
