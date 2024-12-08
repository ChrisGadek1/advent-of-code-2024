import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class Main {
    public enum Direction {
        W (0, -1), E (0, 1), N (-1, 0), S (1, 0), NW (-1, -1), NE (-1, 1), SE (1, 1), SW (1, -1);

        public final int i;
        public  final int j;

        Direction(int i, int j) { this.i = i; this.j = j; }
    }

    public List<Direction> possibleDirections(int i, int j, char value, List<String> grid, Direction currentDirection) {
        return (currentDirection != null ? List.of(currentDirection) : Arrays.stream(Direction.values()).toList())
                .stream()
                .filter(direction -> direction.i + i >= 0 && direction.i + i < grid.size() && direction.j + j >= 0 && direction.j + j < grid.getFirst().length() && grid.get(direction.i + i).charAt(direction.j + j) == value)
                .toList();
    }

    public int numberOfXmasWords(List<String> grid, int i, int j, int lettersIndex, Direction currentDirection) {
        try {
            return possibleDirections(i, j, "XMAS".charAt(lettersIndex + 1), grid, currentDirection)
                    .stream()
                    .map(direction -> numberOfXmasWords(grid, direction.i + i, direction.j + j, lettersIndex + 1, direction))
                    .mapToInt(Integer::intValue)
                    .sum();
        } catch (Exception _) { return 1; }
    }

    public int numberOfAllXmasWords(List<String> grid) {
        return IntStream.range(0, grid.size())
                .flatMap(i -> IntStream.range(0, grid.get(i).length())
                        .filter(j -> grid.get(i).charAt(j) == 'X')
                        .map(j -> numberOfXmasWords(grid, i, j, 0, null)))
                .sum();
    }

    public List<String> readFile(String name) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        return Files.readAllLines(Path.of(Objects.requireNonNull(classLoader.getResource(name)).getFile()));
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();

        System.out.println(main.numberOfAllXmasWords(main.readFile("input.txt")));
    }
}
