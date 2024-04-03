package dk.jonaslindstrom.quevedo.nqueens;

import com.google.common.base.Objects;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {
    private final List<Integer> solution;

    private Solution(List<Integer> solution) {
        this.solution = solution;
    }

    Solution(int init) {
        this.solution = List.of(init);
    }

    public static Set<Solution> fundamentalSolutions(Set<Solution> allSolutions) {
        Set<Solution> solutions = new HashSet<>(allSolutions);
        Set<Solution> fundamentalSolutions = new HashSet<>();
        while (!solutions.isEmpty()) {
            Solution solution = solutions.stream().findAny().get();
            fundamentalSolutions.add(solution);
            solutions.removeAll(solution.symmetricSolutions());
        }
        return fundamentalSolutions;
    }

    /**
     * Check if it is safe to place a queen in the next column of a solution at position y
     */
    private boolean isSafe(int y) {
        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i) == y || Math.abs(i - solution.size()) == Math.abs(solution.get(i) - y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Try to append a queen to the solution at position y. If this is not possible, an empty optional is returned.
     */
    public Optional<Solution> appendIfSafe(int y) {
        if (this.isSafe(y)) {
            List<Integer> newSolution = new ArrayList<>(solution);
            newSolution.add(y);
            return Optional.of(new Solution(newSolution));
        }
        return Optional.empty();
    }

    private Solution mirror() {
        return new Solution(solution.stream().map(y -> solution.size() - y - 1).collect(Collectors.toList()));
    }

    private Solution rotate() {
        return new Solution(IntStream.range(0, solution.size()).mapToObj(y -> solution.indexOf(solution.size() - y - 1)).collect(Collectors.toList()));
    }

    private Set<Solution> symmetricSolutions() {
        Set<Solution> solutions = new HashSet<>();
        solutions.add(this);
        solutions.add(this.mirror());
        solutions.add(this.rotate());
        solutions.add(this.mirror().rotate());
        solutions.add(this.rotate().rotate());
        solutions.add(this.mirror().rotate().rotate());
        solutions.add(this.rotate().rotate().rotate());
        solutions.add(this.mirror().rotate().rotate().rotate());
        return solutions;
    }

    public int size() {
        return solution.size();
    }

    public int get(int i) {
        return solution.get(i);
    }

    public String toString() {
        return solution.stream().map(y -> IntStream.range(0, solution.size()).mapToObj(x -> x == y ? "Q" : ".").collect(Collectors.joining())).collect(Collectors.joining("\n"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution1 = (Solution) o;
        return Objects.equal(solution, solution1.solution);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(solution);
    }
}
