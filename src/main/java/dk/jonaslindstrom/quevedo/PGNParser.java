package dk.jonaslindstrom.quevedo;

import dk.jonaslindstrom.quevedo.moves.Castling;
import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.moves.Promotion;
import dk.jonaslindstrom.quevedo.pieces.Piece;
import dk.jonaslindstrom.quevedo.pieces.Piece.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PGNParser {

    /**
     * Parse a game in PGN format
     */
    public static List<State> parse(String pgn) {

        pgn = pgn.replace("\n", " ");
        pgn = pgn.replace(".", " ");
        String[] tokens = pgn.split(" ");

        List<String> moves = new ArrayList<>();
        int i = 3;
        while (i <= tokens.length) {
            moves.add(tokens[i - 2]);
            moves.add(tokens[i - 1]);
            i += 3; // Move index and two moves
        }

        // A single white move left
        if (i - 2 < tokens.length) {
            moves.add(tokens[i - 2]);
        }

        State state = State.defaultStartingPosition();

        List<State> states = new ArrayList<>();
        states.add(state);

        for (String s : moves) {
            Move move = parse(s, state);
            state = state.apply(move);
            states.add(state);
        }

        return states;
    }

    /**
     * Parse a move in PGN notation. If more or less than one move could correspond to the parsed
     * move, or the resulting move is illegal, an {@link IllegalArgumentException} is thrown
     */
    public static Move parseAndCheck(String move, State state) throws IllegalArgumentException {
        Move parsed = parse(move, state);
        if (!state.legalMoves().contains(parsed)) {
            throw new IllegalArgumentException("Illegal move");
        }
        return parsed;
    }

    /**
     * Parse a move in PGN notation. If more or less than one move could correspond to the parsed
     * move, an {@link IllegalArgumentException} is thrown
     */
    public static Move parse(String move, State state) {
        List<Move> possibleMoves = parseAmbiguous(move, state);

        if (possibleMoves.size() > 1) {
            throw new IllegalArgumentException("Move not unique");
        } else if (possibleMoves.size() == 0) {
            throw new IllegalArgumentException("Move not allowed");
        }
        return possibleMoves.get(0);
    }

    /**
     * Parse a move in PGN notation in the given state and return a list of the moves it could
     * correspond to.
     */
    public static List<Move> parseAmbiguous(String move, State state) {

        // Castling
        if (move.equals("O-O") | move.equals("0-0")) {
            return List.of(new Castling(state.get(7, state.getTurn() == Color.WHITE ? 0 : 7), true));
        } else if (move.equals("O-O-O") | move.equals("0-0-0")) {
            return List.of(new Castling(state.get(0, state.getTurn() == Color.WHITE ? 0 : 7), true));
        }

        // Remove characters not needed for parsing
        move = move
                .replace("!", "")
                .replace("?", "")
                .replace("+", "")
                .replace("#", "")
                .replace("x", "");

        // Promotion
        if (move.contains("=")) {
            String[] parts = move.split("=");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid move");
            }

            String type = parts[1];
            List<Move> possibleMoves = parseAmbiguous(parts[0], state);
            possibleMoves = possibleMoves.stream().filter(m -> m instanceof Promotion)
                    .filter(m -> ((Promotion) m).getType().equals(type)).collect(
                            Collectors.toList());

            return possibleMoves;
        }

        int i = move.length();

        // Target position
        i = i - 2;
        Position to = parsePosition(move.substring(i, i + 2));

        // Piece type
        int j = 0;
        String type = ""; // Pawn by default
        if (move.substring(j, j + 1).matches("[RNBQK]")) {
            type = move.substring(0, 1);
            j = j + 1;
        }

        // All pieces of the right color and type
        Map<Piece, Position> candidates = state.getPieces(type, state.getTurn());

        // Filter moves by the "move to" coordinate
        List<Move> possibleMoves = candidates.keySet().stream()
                .flatMap(piece -> piece.legalMoves(candidates.get(piece), state).stream())
                .filter(x -> x.getTo().equals(to)).collect(
                        Collectors.toList());

        // Castlings has already been handled
        possibleMoves = possibleMoves.stream().filter(m -> !(m instanceof Castling))
                .collect(Collectors.toList());

        // If there's any letters left, they are starting coordinates to uniquely define the move
        if (i != j) {
            String start = move.substring(j, j + 1);
            if (start.matches("[1-8]")) {
                possibleMoves = possibleMoves.stream()
                        .filter(m -> candidates.get(m.getPiece()).y == Integer.parseInt(start) - 1).collect(
                                Collectors.toList());
            } else {
                int x = "abcdefgh".indexOf(start);
                possibleMoves = possibleMoves.stream().filter(m -> candidates.get(m.getPiece()).x == x)
                        .collect(
                                Collectors.toList());
            }
        }

        return possibleMoves;
    }

    public static Position parsePosition(String position) throws IllegalArgumentException {
        if (position.length() != 2) {
            throw new IllegalArgumentException("Position must have exactly two characters");
        }

        int x, y;
        x = "abcdefgh".indexOf(position.substring(0, 1));
        if (x < 0) {
            throw new IllegalArgumentException("Invalid column: " + position.charAt(0));
        }

        try {
            y = Integer.parseInt(position.substring(1, 2)) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid row: " + position.charAt(1));
        }

        return new Position(x, y);
    }

    public static String encode(Move move, State state) {
        String representation;
        boolean capture = state.get(move.getTo()) != null;
        boolean check = state.apply(move).check();
        boolean mate = state.apply(move).mate();
        if (move instanceof Castling) {
            Castling castling = (Castling) move;
            representation = castling.isKingSide() ? "0-0" : "0-0-0";
        } else if (move instanceof Promotion) {
            Promotion promotion = (Promotion) move;
            representation = encode(new Move(move.getPiece(), move.getFrom(), move.getTo()), state);
            representation = representation + "=" + promotion.getType();
        } else {
            String mateOrCheck = "";
            if (mate) {
                mateOrCheck = "#";
            } else if (check) {
                mateOrCheck = "+";
            }

            representation =
                    move.getPiece().getAlgebraicNotation()
                            + move.getFrom()
                            + (capture ? "x" : "")
                            + move.getTo()
                            + mateOrCheck;
        }
        return representation;
    }

}
