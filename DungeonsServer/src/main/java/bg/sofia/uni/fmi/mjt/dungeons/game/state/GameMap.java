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

    private static final int MAP_DIMENSIONS = 20;
    private static final int OBSTACLES_COUNT = 30;
    private static final int MINIONS_COUNT = 10;
    private static final int BOSSES_COUNT = 5;

    private Random generator;
    private Map<Integer, Player> players;

    private Position2D[][] fields;

    public GameMap(Map<Integer, Player> players) {
        this.players = players;
        generator = new Random();
        fields = new Position2D[MAP_DIMENSIONS][MAP_DIMENSIONS];
        constructGameMap();
    }

    public GameMap() {
    }

    // This method should only be called when the player exists - if he is not spawned, use spawnPlayer()
    public void movePlayer(int playerId, Direction direction) {
        Player player = players.get(playerId);
        Position2D previousPosition = player.getPosition();
        int oldX = previousPosition.x();
        int oldY = previousPosition.y();

        Position2D newPosition = switch (direction) {
            case LEFT -> fields[oldX - 1][oldY];
            case RIGHT -> fields[oldX + 1][oldY];
            case UP -> fields[oldX][oldY - 1];
            case DOWN -> fields[oldX][oldY + 1];
        }; // The start of the coordinate system is the upper left corner of the window
        if (canMovePlayerTo(newPosition)) {
            player.setPosition(newPosition);
            previousPosition.removeActor(player);
            newPosition.addActor(player);
        }
    }

    //Will throw NullPointerException if the player does not exist on the map
    public void despawnPlayer(int playerId) {
        Player player = players.get(playerId);
        Position2D currentPosition = player.getPosition();
        currentPosition.removeActor(player);
        players.remove(playerId);
    }

    // Spawns the player at a random free position
    public void spawnPlayer(int playerId) {
        Position2D randomPos = getRandomFreePosition();
        randomPos.addActor(new Player(playerId, randomPos, BattleStats.BASE_PLAYER_STATS));
        players.put(playerId, new Player(playerId, randomPos, BattleStats.BASE_PLAYER_STATS));
    }

    private void constructGameMap() {
        buildBarrier();
        setObstacles();
        spawnMinions();
    }

    private void buildBarrier() {
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                fields[i][j] = new Position2D(i, j);
                if (i == 0 || j == 0 || i == MAP_DIMENSIONS - 1 || j == MAP_DIMENSIONS - 1) {
                    fields[i][j].markAsObstacle();
                }
            }
        }
    }

    private void setObstacles() {
        for (int i = 1; i <= OBSTACLES_COUNT; i++) {
            Position2D randomPos = getRandomFreePosition();
            randomPos.markAsObstacle();
        }
    }

    private void spawnMinions() {
        for (int i = 1; i <= GameMap.MINIONS_COUNT; i++) {
            Position2D randomPos = getRandomSpawnablePosition();
            randomPos.addActor(new Minion());
        }
    }

    // A spawnable position is one that has no actors
    private Position2D getRandomSpawnablePosition() {
        int randomInt = generator.nextInt(MAP_DIMENSIONS * MAP_DIMENSIONS);
        Position2D randomPos = fields[randomInt / MAP_DIMENSIONS][randomInt % MAP_DIMENSIONS];
        while (!randomPos.isSpawnable()) {
            randomInt = generator.nextInt(MAP_DIMENSIONS * MAP_DIMENSIONS);
            randomPos = fields[randomInt / MAP_DIMENSIONS][randomInt % MAP_DIMENSIONS];
        }
        return randomPos;
    }

    private Position2D getRandomFreePosition() {
        int randomInt = generator.nextInt(MAP_DIMENSIONS * MAP_DIMENSIONS);
        Position2D randomPos = fields[randomInt / MAP_DIMENSIONS][randomInt % MAP_DIMENSIONS];
        while (!randomPos.containsFreeSpace()) {
            randomInt = generator.nextInt(MAP_DIMENSIONS * MAP_DIMENSIONS);
            randomPos = fields[randomInt / MAP_DIMENSIONS][randomInt % MAP_DIMENSIONS];
        }
        return randomPos;
    }

    private boolean canMovePlayerTo(Position2D pos) {
        return pos.x() < MAP_DIMENSIONS && pos.y() < MAP_DIMENSIONS && pos.containsFreeSpace();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                out.writeByte(fields[i][j].toByte());
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) {
        throw new UnsupportedOperationException("Map should only be written to clients");
    }

}
