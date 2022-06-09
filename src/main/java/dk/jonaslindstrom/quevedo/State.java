package dk.jonaslindstrom.quevedo;

import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.quevedo.pieces.Piece;
import dk.jonaslindstrom.quevedo.pieces.Piece.Color;
import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.pieces.Bishop;
import dk.jonaslindstrom.quevedo.pieces.King;
import dk.jonaslindstrom.quevedo.pieces.Knight;
import dk.jonaslindstrom.quevedo.pieces.Pawn;
import dk.jonaslindstrom.quevedo.pieces.Queen;
import dk.jonaslindstrom.quevedo.pieces.Rook;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;

public class State {

  private final Map<Move, State> cache = new HashMap<>();
  private final BidiMap<Position, Piece> board;
  private final Piece.Color turn;
  private final Pair<State, Move> parent;

  /**
   * Apply move to generate new state state
   */
  private State(State state, Move move) {
    this.board = move.apply(state.board);
    this.turn = Utils.otherColor(state.turn);

    this.parent = new Pair<>(state, move);
  }

  /** Create a game with the given position */
  private State(Map<Position, Piece> pieces, Piece.Color turn) {
    this.board = new DualHashBidiMap<>(pieces);
    this.turn = turn;
    this.parent = null;
  }

  public Move getLastMove() {
    return parent != null ? parent.getSecond() : null;
  }

  /**
   * Create a new game with default starting position
   */
  public static State defaultStartingPosition() {
    Map<Position, Piece> pieces = new HashMap<>(32);

    for (int i = 0; i < 8; i++) {
      pieces.put(new Position(i, 1), new Pawn(Color.WHITE));
    }
    pieces.put(new Position(0, 0), new Rook(Color.WHITE));
    pieces.put(new Position(1, 0), new Knight(Color.WHITE));
    pieces.put(new Position(2, 0), new Bishop(Color.WHITE));
    pieces.put(new Position(3, 0), new Queen(Color.WHITE));
    pieces.put(new Position(4, 0), new King(Color.WHITE));
    pieces.put(new Position(5, 0), new Bishop(Color.WHITE));
    pieces.put(new Position(6, 0), new Knight(Color.WHITE));
    pieces.put(new Position(7, 0), new Rook(Color.WHITE));

    for (int i = 0; i < 8; i++) {
      pieces.put(new Position(i, 6), new Pawn(Color.BLACK));
    }
    pieces.put(new Position(0, 7), new Rook(Color.BLACK));
    pieces.put(new Position(1, 7), new Knight(Color.BLACK));
    pieces.put(new Position(2, 7), new Bishop(Color.BLACK));
    pieces.put(new Position(3, 7), new Queen(Color.BLACK));
    pieces.put(new Position(4, 7), new King(Color.BLACK));
    pieces.put(new Position(5, 7), new Bishop(Color.BLACK));
    pieces.put(new Position(6, 7), new Knight(Color.BLACK));
    pieces.put(new Position(7, 7), new Rook(Color.BLACK));

    return new State(pieces, Color.WHITE);
  }

  private static Stream<Position> allPositions() {
    return IntStream.range(0, 8).boxed()
        .flatMap(i -> IntStream.range(0, 8).mapToObj(j -> new Position(i, j)));
  }

  /**
   * Get the {@link Piece} located at the given square or <code>null</code> if there is no {@link
   * Piece} on the square or if the square is outside the board.
   *
   * @param x
   * @param y
   * @return
   */
  public Piece get(int x, int y) {
    return get(new Position(x, y));
  }

  /**
   * Get the {@link Piece} located at the given square or <code>null</code> if there is no {@link
   * Piece} on the square or if the square is outside the board.
   *
   * @param position
   * @return
   */
  public Piece get(Position position) {
    return board.getOrDefault(position, null);
  }

  /**
   * Get all pieces of the given type and color still in the current game.
   *
   * @param type
   * @param color
   * @return A map mapping the found pieces to their current position.
   */
  public Map<Piece, Position> getPieces(String type, Color color) {
    return getPieces(piece -> piece.getColor() == color && piece.getAlgebraicNotation().equals(type));
  }

  public Map<Piece, Position> getPieces(Color color) {
    return getPieces(piece -> piece.getColor() == color);
  }

  public Map<Piece, Position> getPieces(Predicate<Piece> predicate) {
    return board.values().stream().filter(predicate).collect(Collectors.toMap(
            piece -> piece,
            piece -> board.inverseBidiMap().get(piece)));
  }

  public boolean check(Color color) {
    return Utils.getKing(color, this).isThreatened(this);
  }

  public List<Move> legalMoves() {
    return getPieces(turn).keySet().stream()
        .flatMap(piece -> piece.legalMoves(board.inverseBidiMap().get(piece), this).stream()).filter(
            this::isValid).collect(Collectors.toList());
  }


  /**
   * Return true if the player to move is in check.
   */
  public boolean check() {
    return check(turn);
  }

  /**
   * Return true if the player to move is mate.
   */
  public boolean mate() {
    if (!check(turn)) {
      return false;
    }

    Map<Piece, Position> myPieces = getPieces(turn);

    return myPieces.keySet().stream()
        .flatMap(piece -> piece.legalMoves(myPieces.get(piece), this).stream())
        .map(this::apply)
        .allMatch(state -> state.check(turn));
  }

  /**
   * Return true if the given {@link Move} is valid and legal in this game
   */
  public boolean isValid(Move move) {

    // No such piece
    if (get(move.getFrom()) == null) {
      return false;
    }

    if (!get(move.getFrom()).equals(move.getPiece())) {
      // Not the right piece
      return false;
    }

    return true;
  }

  /** Get the position of the given {@link Piece} or null if the piece is no longer in the game */
  public Position getPosition(Piece piece) {
    return board.getKey(piece);
  }

  /**
   * Get the game as PGN with extended algebraic notation
   */
  public String getPGN() {
    return getPGNInternal().second.stripLeading();
  }

  private Pair<Integer, String> getPGNInternal() {
    if (parent == null) {
      return new Pair<>(0, "");
    }
    Pair<Integer, String> parentPGN = parent.first.getPGNInternal();

    String pgn = parentPGN.second;
    if (Math.floorMod(parentPGN.first, 2) == 0) {
      pgn = pgn + " " + (parentPGN.first / 2 + 1) + ".";
    }
    pgn = pgn + " " + PGNParser.encode(parent.second, parent.first);
    return new Pair<>(parentPGN.getFirst() + 1, pgn);
  }

  /**
   * Apply the given move and return a new {@link State} with the given
   */
  public State apply(Move move) {
    if (!isValid(move)) {
      throw new IllegalArgumentException("Invalid move");
    }

    // Cache move
    cache.putIfAbsent(move, new State(this, move));
    return cache.get(move);
  }

  public State applyAndDeleteCache(Move move) {
    if (!isValid(move)) {
      throw new IllegalArgumentException("Invalid move");
    }

    State newState;
    if (cache.containsKey(move)) {
      newState = cache.get(move);
    } else {
      newState = new State(this, move);
    }
    cache.clear();
    return newState;
  }

  /**
   * Compute the current score of the game
   */
  public int score() {
    int score = 0;
    for (Piece piece : board.values()) {
      if (piece.getColor() == Color.WHITE) {
        score += piece.getValue();
      } else {
        score -= piece.getValue();
      }
    }
    return score;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("abcdefgh\n");
    for (int y = 7; y >= 0; y--) {
      for (int x = 0; x < 8; x++) {
        Piece p = get(x, y);
        if ((x + y) % 2 == 1) {
          sb.append("\u001B[46m");
        }
        if (p != null) {
          sb.append(p).append("\033[0m");
        } else {
          sb.append(" \033[0m");
        }
      }
      sb.append(y + 1);
      if (y > 0) {
        sb.append("\n");
      }
    }
//    sb.append(turn).append(" to move\n");
//    sb.append("Score: ").append(score()).append("\n");

    return sb.toString();
  }

  public Color getTurn() {
    return turn;
  }

  // TODO: Unnecessary recursion
  public boolean hasMoved(Piece piece) {

    if (parent == null) {
      return false;
    }

    if (parent.second.getPiece().equals(piece)) {
      return true;
    }

    return parent.first.hasMoved(piece);
  }

}
