public enum Quadrant {
    TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NONE;

    public static Quadrant get(int posX, int posY, int gridWidth, int gridHeight) {
        if(posX < gridWidth / 2 && posY < gridHeight / 2) {
            return TOP_LEFT;
        }
        else if(posX > gridWidth / 2 && posY < gridHeight / 2) {
            return TOP_RIGHT;
        }
        else if(posX < gridWidth / 2 && posY > gridHeight / 2) {
            return DOWN_LEFT;
        }
        else if(posX > gridWidth / 2 && posY > gridHeight / 2) {
            return DOWN_RIGHT;
        }
        else {
            return NONE;
        }
    }
}
