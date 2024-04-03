package dk.jonaslindstrom.quevedo;

import dk.jonaslindstrom.quevedo.ai.ConsolePlayer;
import dk.jonaslindstrom.quevedo.ai.NaiveAIPlayer;
import dk.jonaslindstrom.quevedo.ai.Player;
import dk.jonaslindstrom.quevedo.moves.Move;

import java.util.Random;

/**
 * Simple demo of a chess game in a console between a player and a AI.
 */
public class Quevedo {

    public static void main(String[] arguments) throws InterruptedException {
        State state = State.defaultStartingPosition();

        Player white = new ConsolePlayer();
        Player black = new NaiveAIPlayer(new Random(1234));

        boolean isWhite = true;

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

            Thread.sleep(100);

            Move move;
            if (isWhite) {
                move = white.apply(state);
            } else {
                move = black.apply(state);
            }

            state = state.applyAndDeleteCache(move);
            System.out.println(move);
            System.out.println(state.getPGN());
            isWhite = !isWhite;

            Thread.sleep(100);

            System.out.println();
        }

        System.out.println();
        System.out.println(state.getPGN());
    }

}
