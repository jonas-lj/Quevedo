package dk.jonaslindstrom.quevedo.nqueens;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class NQueensSolver {

    private final int n;

    public NQueensSolver(int n) {
        this.n = n;
    }

    public static void main(String[] args) {
        NQueensSolver nQueens = new NQueensSolver(7);
        Set<Solution> solutions = nQueens.findAll();
        System.out.println("Solutions:   " + solutions.size());

        Set<Solution> fundamentalSolutions = Solution.fundamentalSolutions(solutions);
        System.out.println("Fundamental solutions: " + fundamentalSolutions.size());
        fundamentalSolutions.forEach(s -> {
            System.out.println(s);
            System.out.println();
        });
    }

    public Set<Solution> findAll() {
        return IntStream.range(0, n).mapToObj(Solution::new).parallel().flatMap(this::solveInternal).collect(Collectors.toSet());
    }

    private Stream<Solution> solveInternal(Solution candidate) {
        if (candidate.size() == n) {
            // Found a solution
            return Stream.of(candidate);
        }
        // Otherwise, try to place a queen in the next column
        return IntStream.range(0, n)
                .mapToObj(candidate::appendIfSafe)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(this::solveInternal);
    }
}