package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Castling;
import dk.jonaslindstrom.quevedo.moves.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    protected List<Move> possibleMoves(Position position, State state) {
        List<Move> result = new ArrayList<>();
        result.addAll(state.untilIllegal(IntStream.range(1, n)
                .mapToObj(i -> new Move(this, position, position.transpose(i, 0)))));
        result.addAll(state.untilIllegal(IntStream.range(1, n)
                .mapToObj(i -> new Move(this, position, position.transpose(-i, 0)))));
        result.addAll(state.untilIllegal(IntStream.range(1, n)
                .mapToObj(i -> new Move(this, position, position.transpose(0, i)))));
        result.addAll(state.untilIllegal(IntStream.range(1, n)
                .mapToObj(i -> new Move(this, position, position.transpose(0, -i)))));

        // Castlings are assigned to rooks since they define them uniquely
        int y = this.getColor() == Color.WHITE ? 0 : 7;
        if (!state.hasMoved(this) && position.y == y) { // Ensure promoted rooks cannot castle
            Piece king = state.get(4, y);
            if (king != null && !state.hasMoved(king)) {
                if (state.getPosition(this).x == 0) {
                    if (state.get(1, y) == null && state.get(2, y) == null && state.get(3, y) == null) {
                        result.add(new Castling(this, false));
                    }
                } else {
                    if (state.get(6, y) == null && state.get(5, y) == null) {
                        result.add(new Castling(this, true));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public String getAlgebraicNotation() {
        return "R";
    }

    public String toString() {
        if (getColor() == Color.WHITE) {
            return "♖";
        } else {
            return "♜";
        }
    }

}
