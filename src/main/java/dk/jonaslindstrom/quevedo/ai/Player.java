package dk.jonaslindstrom.quevedo.ai;

import dk.jonaslindstrom.quevedo.State;
import dk.jonaslindstrom.quevedo.moves.Move;

import java.util.function.Function;

public interface Player extends Function<State, Move> {

}
