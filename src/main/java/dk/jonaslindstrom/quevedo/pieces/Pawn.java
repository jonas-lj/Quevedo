package dk.jonaslindstrom.quevedo.pieces;

import dk.jonaslindstrom.quevedo.moves.EnPassant;
import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.Position;
import dk.jonaslindstrom.quevedo.moves.Promotion;
import dk.jonaslindstrom.quevedo.State;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Pawn extends Piece {

  public Pawn(Color color) {
    super(color);
  }

  @Override
  public int getValue() {
    return 1;
  }

  @Override
  public List<Move> legalMoves(Position position, State state) {
    List<Move> result = new ArrayList<>();

    /* White pawn */
    if (getColor() == Color.WHITE) {

      if (state.get(position.add(0, 1)) == null) {

        // No piece in front of pawn
        if (position.y < 6) {
          result.add(new Move(this, position, position.add(0, 1)));
          if (!state.hasMoved(this) && state.get(position.add(0, 2)) == null) {
            // First move and no pieces in the two squares in front of pawn
            result.add(new Move(this, position, position.add(0, 2)));
          }
        } else {
          // Promotion
          result.addAll(Promotion.allPromotions(this, position, position.add(0, 1)));
        }
      }

      if (state.get(position.add(1, 1)) != null
          && state.get(position.add(1, 1)).getColor() == Color.BLACK) {

      // A black piece to the front right

        if (position.y < 6) {
          // Capture piece
          result.add(new Move(this, position, position.add(1, 1)));
        } else {
          // Promotion
          result.addAll(Promotion.allPromotions(this, position, position.add(1, 1)));
        }
      }

      if (state.get(position.add(-1, 1)) != null
          && state.get(position.add(-1, 1)).getColor() == Color.BLACK) {

        // A black piece to the front left

        if (position.y < 6) {
          result.add(new Move(this, position, position.add(-1, 1)));
        } else {
          // Promotion
          result.addAll(Promotion.allPromotions(this, position, position.add(-1, 1)));
        }
      }

      Move latest = state.getLastMove();
      if (latest != null) {

        // Check for en passant
        if (latest.getPiece().getAlgebraicNotation().equals("")) {
          Piece piece = state.get(-1, 0);
          if (piece != null) {
            if (piece.equals(latest.getPiece())
                && latest.getFrom().equals(position.add(-1, 2))) {
              result.add(new EnPassant(this, position, position.add(-1, 1)));
            }
          }

          piece = state.get(position.add(1, 0));
          if (piece != null) {
            if (piece.equals(latest.getPiece())
                && latest.getFrom().equals(position.add(1, 2))) {
              result.add(new EnPassant(this, position, position.add(
                  1, 1)));
            }
          }
        }
      }

    } else {

      /* Black pawn */

      if (state.get(position.add(0, -1)) == null) {
        if (position.y == 1) {
          result.addAll(Promotion.allPromotions(this, position, position.add(0, -1)));
        } else {
          result.add(new Move(this, position, position.add(0, -1)));
          if (!state.hasMoved(this) && state.get(position.add(0, -2)) == null) {
            result.add(new Move(this, position, position.add(0, -2)));
          }
        }
      }

      if (state.get(position.add(1, -1)) != null
          && state.get(position.add(1, -1)).getColor() == Color.WHITE) {
        if (position.y == 1) {
          result.addAll(Promotion.allPromotions(this, position, position.add(1, -1)));
        } else {
          result.add(new Move(this, position, position.add(1, -1)));
        }
      }

      if (state.get(position.add(-1, -1)) != null
          && state.get(position.add(-1, -1)).getColor() == Color.WHITE) {
        if (position.y == 1) {
          result.addAll(Promotion.allPromotions(this, position, position.add(-1, -1)));
        } else {
          result.add(new Move(this, position, position.add(-1, -1)));
        }
      }
    }

    Move latest = state.getLastMove();
    if (latest != null) {

      // Check for en passant
      if (latest.getPiece().getAlgebraicNotation().equals("")) {
        Piece piece = state.get(position.add(-1, 0));
        if (piece != null) {
          if (piece.equals(latest.getPiece())
              && latest.getFrom().equals(position.add(-1, -2))) {
            result.add(new EnPassant(this, position, position.add(
                -1, -1)));
          }
        }

        piece = state.get(position.add(1, 0));
        if (piece != null) {
          if (piece.equals(latest.getPiece())
              && latest.getFrom().equals(position.add(1, -2))) {
            result.add(new EnPassant(this, position, position.add(
                1, -1)));
          }
        }

      }
    }

    // Filter out moves outside board
    result = result.stream().filter(move -> !Piece.outsideBoard(move.getTo()))
        .collect(Collectors.toList());

    return result;
  }

  @Override
  public String getAlgebraicNotation() {
    return "";
  }

  public String toString() {
    if (getColor() == Color.WHITE) {
      return "♙";
    } else {
      return "♟";
    }
  }

}
