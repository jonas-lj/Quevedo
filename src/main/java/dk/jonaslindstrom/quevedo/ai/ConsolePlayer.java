package dk.jonaslindstrom.quevedo.ai;

import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.PGNParser;
import dk.jonaslindstrom.quevedo.State;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
