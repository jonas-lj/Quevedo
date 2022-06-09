package dk.jonaslindstrom.quevedo;

import dk.jonaslindstrom.quevedo.ai.ConsolePlayer;
import dk.jonaslindstrom.quevedo.ai.Player;
import dk.jonaslindstrom.quevedo.moves.Move;

/**
 * Simple demo of a chess game in a console between a player and a AI.
 */
public class Quevedo {

  public static void main(String[] arguments) throws InterruptedException {
    State state = State.defaultStartingPosition();

    Player white = new ConsolePlayer(); //new RandomPlayer(new Random(1234));
    Player black = new ConsolePlayer();

    boolean isWhite = true;

    int i = 0;

    while (true) {

      System.out.println("Move " + (i / 2) + 1);
      System.out.println(state);

      if (state.mate()) {
        System.out.println("Mate");
        break;
      }

      if (state.check()) {
        System.out.println("Check");
      }
      System.out.println("Turn: " + state.getTurn());

      Thread.sleep(1000);

      Move move;
      if (isWhite) {
        move = white.apply(state);
      } else {
        move = black.apply(state);
      }

      if (!state.legalMoves().contains(move)) {
        throw new IllegalArgumentException("Illegal move: " + move);
      }

      state = state.applyAndDeleteCache(move);
      System.out.println(move);
      isWhite = !isWhite;
      i++;
      Thread.sleep(1000);

    }

    System.out.println(state.getPGN());
  }

}
