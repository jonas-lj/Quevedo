package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.Utils;
import dk.jonaslindstrom.quevedo.moves.Move;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Piece {

    protected final int n;
    private final Color color;

    protected Piece(Color color, int n) {
        this.color = color;
        this.n = n;
    }

    protected Piece(Color color) {
        this(color, 8);
    }

    public Color getColor() {
        return color;
    }

    public abstract int getValue();

    /**
     * Returns a list of moves this piece can make in the current state.
     *
     * @see #possibleMoves(Position, State)
     */
    public List<Move> legalMoves(Position position, State state) {
        List<Move> possibleMoves = possibleMoves(position, state);

        // Filter out moves that puts the own players king in check
        possibleMoves = possibleMoves.stream()
                .filter(move -> !state.apply(move).check(color)).collect(Collectors.toList());

        return possibleMoves;
    }

    /**
     * Return a list of all possible moves this piece can make in the current state. This does not account for moves that
     * will put the players own king in check.
     *
     * @see #legalMoves(Position, State)
     */
    protected abstract List<Move> possibleMoves(Position position, State state);

    public abstract String getAlgebraicNotation();

    public boolean isThreatened(State state) {
        Map<Piece, Position> opponentsPieces = state.getPieces(Utils.otherColor(color));
        return opponentsPieces.keySet().stream()
                .flatMap(piece -> piece.possibleMoves(opponentsPieces.get(piece), state).stream())
                .anyMatch(move -> move.getTo().equals(state.getPosition(this)));
    }

    public enum Color {
        WHITE, BLACK
    }

}
