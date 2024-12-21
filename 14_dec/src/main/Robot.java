import java.util.Arrays;
import java.util.Map;

public class Robot {
    private int posX;
    private int posY;
    private final int velX;
    private final int velY;
    private final int gridWidth;
    private final int gridHeight;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Robot(int posX, int posY, int velX, int velY, int gridWidth, int gridHeight) {
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    public void move(int times, Map<Integer, Map<Integer, Robot>> robotsMap) {
        robotsMap.get(this.posX).put(this.posY, null);
        int newPosX = (this.posX + this.velX * times) % gridWidth < 0 ? (this.posX + this.velX * times) % gridWidth  + gridWidth : (this.posX + this.velX * times) % gridWidth;
        int newPosY = (this.posY + this.velY * times) % gridHeight < 0 ? (this.posY + this.velY * times) % gridHeight + gridHeight : (this.posY + this.velY * times) % gridHeight;
        this.posX = newPosX % gridWidth;
        this.posY = newPosY % gridHeight;
        robotsMap.get(this.posX).put(this.posY, this);
    }

    public static Robot loadFromString(String line, int gridWidth, int gridHeight) {
        int posX = Integer.parseInt(line.split(" ")[0].split("=")[1].split(",")[0]);
        int posY = Integer.parseInt(line.split(" ")[0].split("=")[1].split(",")[1]);
        int velX = Integer.parseInt(line.split(" ")[1].split("=")[1].split(",")[0]);
        int velY = Integer.parseInt(line.split(" ")[1].split("=")[1].split(",")[1]);
        return new Robot(posX, posY, velX, velY, gridWidth, gridHeight);
    }

    public long getNumberOfNeighbours(Map<Integer, Map<Integer, Robot>> robotsMap) {
        int[][] directions = { {-1, -1}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1} };
        return Arrays
                .stream(directions)
                .filter(dir -> isInGrid(posX + dir[0], posY + dir[1], gridWidth, gridHeight))
                .filter(dir -> robotsMap.get(posX + dir[0]).get(posY + dir[1]) != null)
                .count();
    }

    private boolean isInGrid(int posX, int posY, int gridWidth, int gridHeight) {
        return posX >= 0 && posX < gridWidth && posY >= 0 && posY < gridHeight;
    }

    @Override
    public String toString() {
        return "pos: (" + this.posX + "," + this.posY+")"+", vel: ("+this.velX+","+this.velY+")";
    }
}
