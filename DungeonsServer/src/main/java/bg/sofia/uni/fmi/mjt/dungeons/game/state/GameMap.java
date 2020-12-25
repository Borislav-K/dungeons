package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

public class GameMap implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int SQUARE_SIDE = 20; //TODO increase
    private static final int OBSTACLES_COUNT = 30;

    private static final char EMPTY_SPACE = '.';
    private static final char OBSTACLE = '#';
    private static final char TREASURE = 'T'; //TODO generate on bootstrap
    private static final char MINION = 'M'; //TODO generate on bootstrap

    private transient Random generator;
    private transient Map<Integer, Position2D> players;

    private char[][] fields;

    public GameMap(Map<Integer, Position2D> players) {
        this.players = players;
        generator = new Random();
        fields = new char[SQUARE_SIDE][SQUARE_SIDE];
        constructGameMap();
    }

    // This method should only be called when the player exists - if he is not spawned, use spawnPlayer()
    public void movePlayer(int playerId, Direction direction) {
        Position2D previousPosition = players.get(playerId);
        int oldX = previousPosition.x();
        int oldY = previousPosition.y();

        Position2D newPosition = switch (direction) {
            case LEFT -> new Position2D(oldX - 1, oldY);
            case RIGHT -> new Position2D(oldX + 1, oldY);
            case UP -> new Position2D(oldX, oldY - 1);
            case DOWN -> new Position2D(oldX, oldY + 1);
        }; // The start of the coordinate system is the upper left corner of the window
        if (isFreeSpace(newPosition)) {
            players.put(playerId, newPosition);
            fields[oldX][oldY] = EMPTY_SPACE;
            fields[newPosition.x()][newPosition.y()] = (char) ('0' + playerId);
        }
    }

    //Will throw NullPointerException if the player does not exist on the map
    public void despawnPlayer(int playerId) {
        Position2D currentPosition = players.get(playerId);
        fields[currentPosition.x()][currentPosition.y()] = EMPTY_SPACE;
    }

    // Spawns the player at a random free position
    public void spawnPlayer(int playerId) {
        Position2D randomPos = getRandomFreePosition();
        fields[randomPos.x()][randomPos.y()] = (char) ('0' + playerId);
        players.put(playerId, randomPos);
    }

    private void constructGameMap() {
        buildBarrier();
        setObstacles();
    }

    private void buildBarrier() {
        for (int i = 0; i < SQUARE_SIDE; i++) {
            for (int j = 0; j < SQUARE_SIDE; j++) {
                if (i == 0 || j == 0 || i == SQUARE_SIDE - 1 || j == SQUARE_SIDE - 1) {
                    fields[i][j] = OBSTACLE;
                } else {
                    fields[i][j] = EMPTY_SPACE;
                }
            }
        }
    }

    private void setObstacles() {
        for (int i = 1; i <= OBSTACLES_COUNT; i++) {
            Position2D randomPos = getRandomFreePosition();
            fields[randomPos.x()][randomPos.y()] = OBSTACLE;
        }
    }

    private Position2D getRandomFreePosition() {
        int randomInt = generator.nextInt(SQUARE_SIDE * SQUARE_SIDE);
        Position2D randomPos = new Position2D(randomInt / SQUARE_SIDE, randomInt % SQUARE_SIDE);
        while (!isFreeSpace(randomPos)) {
            randomInt = generator.nextInt(SQUARE_SIDE * SQUARE_SIDE);
            randomPos = new Position2D(randomInt / SQUARE_SIDE, randomInt % SQUARE_SIDE);
        }
        return randomPos;
    }

    private boolean isFreeSpace(Position2D position2D) {
        int x = position2D.x();
        int y = position2D.y();
        return x < SQUARE_SIDE && y < SQUARE_SIDE &&
               fields[x][y] == EMPTY_SPACE;
    }
}
