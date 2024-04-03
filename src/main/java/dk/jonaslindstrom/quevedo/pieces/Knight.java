package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Move;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    protected List<Move> possibleMoves(Position position, State state) {
        return Stream.of(
                        position.transpose(2, 1),
                        position.transpose(2, -1),
                        position.transpose(-2, 1),
                        position.transpose(-2, -1),
                        position.transpose(1, 2),
                        position.transpose(1, -2),
                        position.transpose(-1, 2),
                        position.transpose(-1, -2))
                .filter(p -> !state.outsideBoard(p))
                .filter(p -> state.get(p) == null || state.get(p).getColor() != this.getColor())
                .map(p -> new Move(this, position, p))
                .collect(Collectors.toList());
    }

    @Override
    public String getAlgebraicNotation() {
        return "N";
    }

    public String toString() {
        if (getColor() == Color.WHITE) {
            return "♘";
        } else {
            return "♞";
        }
    }

}
