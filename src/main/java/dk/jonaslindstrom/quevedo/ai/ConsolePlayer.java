package dk.jonaslindstrom.quevedo.ai;

import dk.jonaslindstrom.quevedo.PGNParser;
import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.pieces.Piece;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ConsolePlayer implements Player {

    private final BufferedReader reader;

    public ConsolePlayer() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Move apply(State state) {
        try {
            String input = reader.readLine();

            try {
                if (input.startsWith("L")) {
                    Position position = PGNParser.parsePosition(input.substring(1));
                    Piece piece = state.get(position);
                    if (piece == null || piece.getColor() != state.getTurn()) {
                        System.out.println("No piece on position " + position);
                        return apply(state);
                    }
                    System.out.println(piece.legalMoves(position, state));
                    return apply(state);
                } else if (input.equals("T")) {
                    System.out.println(state.getPieces(state.getTurn()).keySet().stream().
                            filter(piece -> piece.isThreatened(state)).map(piece -> piece.toString() + " " + state.getPosition(piece)
                            ).collect(
                                    Collectors.toList()));
                    return apply(state);
                } else if (input.equals("P")) {
                    System.out.println(state.legalMoves());
                    return apply(state);
                }

                return PGNParser.parseAndCheck(input, state);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to parse move '" + input + "': " + e.getMessage());
                return apply(state);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
