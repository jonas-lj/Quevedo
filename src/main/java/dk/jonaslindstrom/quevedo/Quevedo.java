package dk.jonaslindstrom.xerxes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Simple demo of a chess game in a console using the Xerces engine */
public class Quevedo {

  public static void main(String[] arguments) {
    State state = State.defaultStartingPosition();

    int status = 0;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      System.out.println(state);

      if (state.mate()) {
        System.out.println("Mate");
        break;
      }

      if (state.check()) {
        System.out.println("Check");
      }
      System.out.println("Turn: " + state.getTurn());

      Move move = null;
      try {
        String input = reader.readLine();

        try {
          move = PGNParser.parse(input, state);
        } catch (Exception e) {
          System.out.println("Unable to parse move '" + input + "': " + e.getMessage());
          continue;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      state = state.applyAndDeleteCache(move);

    }

    System.out.println(state.getPGN());

  }

}
