package dk.jonaslindstrom.quevedo.ai;

import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Move;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class NaiveAIPlayer implements Player {

    private final Random random;

    public NaiveAIPlayer(Random random) {
        this.random = random;
    }

    @Override
    public Move apply(State state) {
        List<Move> moves = state.legalMoves();

        List<Pair<Move, Integer>> analysed = moves.stream().map(move -> new Pair<>(move, state.apply(move).score(state.getTurn()))).collect(
                Collectors.toList());

        Pair<Move, Integer> best = analysed.stream().max(Comparator.comparingInt(a -> a.second)).get();

        return best.first;
    }
}
