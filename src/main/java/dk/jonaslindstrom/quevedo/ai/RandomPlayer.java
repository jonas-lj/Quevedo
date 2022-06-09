package dk.jonaslindstrom.quevedo.ai;

import dk.jonaslindstrom.quevedo.moves.Move;
import dk.jonaslindstrom.quevedo.State;
import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player {

  private final Random random;

  public RandomPlayer(Random random) {
    this.random = random;
  }

  @Override
  public Move apply(State state) {
    List<Move> moves = state.legalMoves();
    int moveIndex = random.nextInt(moves.size());
    return moves.get(moveIndex);
  }
}
