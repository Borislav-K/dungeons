package bg.sofia.uni.fmi.mjt.dungeons.game.map;

import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameMap {

    private static final int SQUARE_SIDE = 10; //TODO increase
    private static final int OBSTACLES_COUNT = 10;

    private static final char EMPTY_SPACE = '.';
    private static final char OBSTACLE = '#';
    private static final char TREASURE = 'T'; //TODO generate on bootstrap
    private static final char MINION = 'M'; //TODO generate on bootstrap

    private Random generator;
    private char[][] fields;

    private Map<Integer, Integer> players;

    public GameMap() {
        this.players = new HashMap<>();
        generator = new Random();
        fields = new char[SQUARE_SIDE][SQUARE_SIDE];
        constructGameMap();
    }

    public byte[] serialize() {
        byte[] result = new byte[SQUARE_SIDE * SQUARE_SIDE];
        for (int i = 0; i < SQUARE_SIDE; i++) {
            for (int j = 0; j < SQUARE_SIDE; j++) {
                result[i * SQUARE_SIDE + j] = (byte) fields[i][j];
            }
        }
        return result;
    }

    public char[][] getMap() {
        return fields;
    }


    // This method should only be called when the player exists - if he is not spawned, use spawnPlayer()
    public void movePlayer(int playerId, int newX, int newY) {
        if (!isFreeSpace(newX, newY)) {
            return;
        }
        int previousPosition = players.get(playerId);
        int oldX = previousPosition / SQUARE_SIDE;
        int oldY = previousPosition % SQUARE_SIDE;
        fields[oldX][oldY] = EMPTY_SPACE;

        players.put(playerId, newX * SQUARE_SIDE + newY);
        fields[newX][newY] = (char) ('0' + playerId);
    }

    // This method should only be called when the player exists - if he is not spawned, use spawnPlayer()
    public void movePlayer(int playerId, Direction direction) {
        int previousPosition = players.get(playerId);
        int oldX = previousPosition / SQUARE_SIDE;
        int oldY = previousPosition % SQUARE_SIDE;

        int newPosition = switch (direction) {
            case LEFT -> previousPosition - 1;
            case RIGHT -> previousPosition + 1;
            case UP -> previousPosition - SQUARE_SIDE;
            case DOWN -> previousPosition + SQUARE_SIDE;
        };
        int newX = newPosition / SQUARE_SIDE;
        int newY = newPosition % SQUARE_SIDE;
        if (isFreeSpace(newX, newY)) {
            players.put(playerId, newPosition);
            fields[oldX][oldY] = EMPTY_SPACE;
            fields[newX][newY] = (char) ('0' + playerId);
        }
    }

    //Will throw NullPointerException if the player does not exist on the map
    public void removePlayer(int playerId) {
        int currentPosition = players.get(playerId);
        int x = currentPosition / SQUARE_SIDE;
        int y = currentPosition % SQUARE_SIDE;
        fields[x][y] = EMPTY_SPACE;
    }

    // Spawns the player at a random free position
    public void spawnPlayer(int playerId) {
        int randomPos = getRandomFreePosition();
        fields[randomPos / SQUARE_SIDE][randomPos % SQUARE_SIDE] = (char) ('0' + playerId);
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
            int randomPos = getRandomFreePosition();
            int randomX = randomPos / SQUARE_SIDE;
            int randomY = randomPos % SQUARE_SIDE;
            fields[randomX][randomY] = OBSTACLE;
        }
    }

    private int getRandomFreePosition() {
        int randomPos = generator.nextInt(SQUARE_SIDE * SQUARE_SIDE);
        while (!isFreeSpace(randomPos / SQUARE_SIDE, randomPos % SQUARE_SIDE)) {
            randomPos = generator.nextInt(SQUARE_SIDE * SQUARE_SIDE);
        }
        return randomPos;
    }

    private boolean isFreeSpace(int x, int y) {
        return x < SQUARE_SIDE && y < SQUARE_SIDE &&
               fields[x][y] == EMPTY_SPACE;
    }

}
