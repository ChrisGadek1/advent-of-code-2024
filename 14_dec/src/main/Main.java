import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    List<String> loadFile(String path) throws URISyntaxException, IOException {
        return Files.readAllLines(Path.of(Objects.requireNonNull(getClass().getClassLoader().getResource(path)).toURI()));
    }

    public int numberOfAdjacentRobots(List<Robot> robots, Map<Integer, Map<Integer, Robot>> robotsMap) {
        int counter = 0;
        for(Robot robot: robots) {
            if(robot.getNumberOfNeighbours(robotsMap) > 1) {
                counter++;
            }
        }
        return counter;
    }

    public void printRobots(Map<Integer, Map<Integer, Robot>> robotsMap, int gridWidth, int gridHeight) {
        for(int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                System.out.print(robotsMap.get(i).get(j) == null ? "." : "*");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) throws URISyntaxException, IOException {
        Main main = new Main();
        List<String> lines = main.loadFile("input.txt");
        int gridWidth = 101, gridHeight = 103, seconds = 100;
        Map<Integer, Map<Integer, Robot>> robotsMap = new HashMap<>();
        for (int i = 0; i < gridWidth; i++) {
            robotsMap.put(i, new HashMap<>());
            for (int j = 0; j < gridHeight; j++) {
                robotsMap.get(i).put(j, null);
            }
        }

        List<Robot> robotList = lines.stream().map(line -> Robot.loadFromString(line, gridWidth, gridHeight)).toList();
        robotList.forEach(robot -> robotsMap.get(robot.getPosX()).put(robot.getPosY(), robot));
// Part 1
//        robotList.forEach(robot -> robot.move(seconds, robotsMap));
//        List<Quadrant> quadrantList = robotList.stream().map(robot -> Quadrant.get(robot.getPosX(), robot.getPosY(), gridWidth, gridHeight)).toList();
//        Map<Quadrant, Integer> result = new HashMap<>();
//        for(Quadrant quadrant: quadrantList) {
//            if(quadrant != Quadrant.NONE) {
//                if(!result.containsKey(quadrant)) {
//                    result.put(quadrant, 0);
//                }
//                result.put(quadrant, result.get(quadrant) + 1);
//            }
//        }
//        long resultValue = result.values().stream().reduce(1, (a, b) -> a * b);
//        System.out.println(resultValue);

        // Part 2
        seconds = 1;
        while(seconds < 10000) {
            robotList.forEach(robot -> robot.move(1, robotsMap));
            if(main.numberOfAdjacentRobots(robotList, robotsMap) >= 256) {
                main.printRobots(robotsMap, gridWidth, gridHeight);
                System.out.println(seconds);
                break;
            }
            seconds++;
        }
    }
}