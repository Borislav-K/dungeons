package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import java.util.Random;

public class GameMap implements Externalizable {

    private static final long serialVersionUID = 1L;

    private static final int MAP_DIMENSIONS = 20; //TODO increase
    private static final int OBSTACLES_COUNT = 30;

    private static final char EMPTY_SPACE = '.';
    private static final char OBSTACLE = '#';
    private static final char TREASURE = 'T'; //TODO generate on bootstrap
    private static final char MINION = 'M'; //TODO generate on bootstrap

    private Random generator;
    private Map<Integer, Player> players;

    private char[][] fields;

    public GameMap(Map<Integer, Player> players) {
        this.players = players;
        generator = new Random();
        fields = new char[MAP_DIMENSIONS][MAP_DIMENSIONS];
        constructGameMap();
    }

    // This method should only be called when the player exists - if he is not spawned, use spawnPlayer()
    public void movePlayer(int playerId, Direction direction) {
        Player player = players.get(playerId);
        Position2D previousPosition = player.getPosition();
        int oldX = previousPosition.x();
        int oldY = previousPosition.y();

        Position2D newPosition = switch (direction) {
            case LEFT -> new Position2D(oldX - 1, oldY);
            case RIGHT -> new Position2D(oldX + 1, oldY);
            case UP -> new Position2D(oldX, oldY - 1);
            case DOWN -> new Position2D(oldX, oldY + 1);
        }; // The start of the coordinate system is the upper left corner of the window
        if (isFreeSpace(newPosition)) {
            player.setPosition(newPosition);
            fields[oldX][oldY] = EMPTY_SPACE;
            fields[newPosition.x()][newPosition.y()] = (char) ('0' + playerId);
        }
    }

    //Will throw NullPointerException if the player does not exist on the map
    public void despawnPlayer(int playerId) {
        Position2D currentPosition = players.get(playerId).getPosition();
        fields[currentPosition.x()][currentPosition.y()] = EMPTY_SPACE;
        players.remove(playerId);
    }

    // Spawns the player at a random free position
    public void spawnPlayer(int playerId) {
        Position2D randomPos = getRandomFreePosition();
        fields[randomPos.x()][randomPos.y()] = (char) ('0' + playerId);
        players.put(playerId, new Player(randomPos));
    }

    private void constructGameMap() {
        buildBarrier();
        setObstacles();
    }

    private void buildBarrier() {
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                if (i == 0 || j == 0 || i == MAP_DIMENSIONS - 1 || j == MAP_DIMENSIONS - 1) {
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
        int randomInt = generator.nextInt(MAP_DIMENSIONS * MAP_DIMENSIONS);
        Position2D randomPos = new Position2D(randomInt / MAP_DIMENSIONS, randomInt % MAP_DIMENSIONS);
        while (!isFreeSpace(randomPos)) {
            randomInt = generator.nextInt(MAP_DIMENSIONS * MAP_DIMENSIONS);
            randomPos = new Position2D(randomInt / MAP_DIMENSIONS, randomInt % MAP_DIMENSIONS);
        }
        return randomPos;
    }

    private boolean isFreeSpace(Position2D position2D) {
        int x = position2D.x();
        int y = position2D.y();
        return x < MAP_DIMENSIONS && y < MAP_DIMENSIONS &&
               fields[x][y] == EMPTY_SPACE;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                out.writeByte(fields[i][j]);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                fields[i][j] = (char) in.readByte();
            }
        }
    }

}
