package dk.jonaslindstrom.xerxes;

import dk.jonaslindstrom.xerxes.Piece.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PGNParser {

  /** Parse a game in PGN */
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

  public static Move parse(String move, State state) {

    // Castling
    if (move.equals("O-O") | move.equals("0-0")) {
      return new Castling(state.get(7, state.getTurn() == Color.WHITE ? 0 : 7), true);
    } else if (move.equals("O-O-O") | move.equals("0-0-0")) {
      return new Castling(state.get(0, state.getTurn() == Color.WHITE ? 0 : 7), true);
    }
    int i = move.length();

    // Is check or mate
    if (move.charAt(i - 1) == '+' | move.charAt(i - 1) == '#') {
      i = i - 1;
    }

    // Target position
    i = i - 2;
    Position to = parsePosition(move.substring(i, i + 2));

    // Capture
    if (i > 0 && move.charAt(i - 1) == 'x') {
      i = i - 1;
    }

    // Piece type
    int j = 0;
    String type = ""; // Pawn by default
    if (move.substring(j, j + 1).matches("[RNBQK]")) {
      type = move.substring(0, 1);
      j = j + 1;
    }

    // All pieces of the right color and type
    Map<Piece, Position> candidates = state.getPieces(type, state.getTurn());

    List<Piece> candidate = candidates.keySet().stream()
        .filter(piece -> piece.legalMoves(candidates.get(piece), state).stream().map(Move::getTo).anyMatch(p -> p.equals(to))).collect(
            Collectors.toList());

    // If there's any letters left, they are starting coordinates
    if (i != j) {
      String start = move.substring(j, j+1);
      if (start.matches("[1-8]")) {
        candidate = candidate.stream().filter(piece -> candidates.get(piece).y == Integer.parseInt(start) - 1).collect(
            Collectors.toList());
      } else {
        int x = "abcdefgh".indexOf(start);
        candidate = candidate.stream().filter(piece -> candidates.get(piece).x == x).collect(
            Collectors.toList());
      }
    }

    if (candidate.size() > 1) {
      throw new IllegalArgumentException("Move not unique");
    } else if (candidate.size() == 0) {
      throw new IllegalArgumentException("Move not allowed");
    }
    Piece piece = candidate.get(0);
    return new Move(piece, candidates.get(piece), to);
  }

  private static Position parsePosition(String position) {
    int x = "abcdefgh".indexOf(position.substring(0, 1));
    int y = Integer.parseInt(position.substring(1, 2)) - 1;
    return new Position(x, y);
  }

}
