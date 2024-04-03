package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    protected List<Move> possibleMoves(Position position, State state) {
        List<Move> result = new ArrayList<>();
        result.addAll(state.untilIllegal(IntStream.range(1, super.n)
                .mapToObj(i -> new Move(this, position, position.transpose(i, i)))));
        result.addAll(state.untilIllegal(IntStream.range(1, super.n)
                .mapToObj(i -> new Move(this, position, position.transpose(-i, -i)))));
        result.addAll(state.untilIllegal(IntStream.range(1, super.n)
                .mapToObj(i -> new Move(this, position, position.transpose(i, -i)))));
        result.addAll(state.untilIllegal(IntStream.range(1, super.n)
                .mapToObj(i -> new Move(this, position, position.transpose(-i, i)))));
        return result;
    }

    @Override
    public String getAlgebraicNotation() {
        return "B";
    }

    public String toString() {
        if (getColor() == Color.WHITE) {
            return "♗";
        } else {
            return "♝";
        }
    }

}
