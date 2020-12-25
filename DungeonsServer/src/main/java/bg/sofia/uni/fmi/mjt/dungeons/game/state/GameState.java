package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;
import bg.sofia.uni.fmi.mjt.dungeons.game.io.PerformantByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private GameMap gameMap;

    private transient Map<Integer, Position2D> players;

    public GameState() {
        this.players = new HashMap<>();
        this.gameMap = new GameMap(players);
    }

    public byte[] serialize() {
        try (PerformantByteArrayOutputStream output = new PerformantByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(output)) {
            objectOutputStream.writeObject(this);
            return output.getBuf();
        } catch (IOException e) {
            throw new RuntimeException("Could not serialize the game state", e);
        }
    }

    // This method should only be called when the player exists - if he is not spawned, use spawnPlayer()
    public void movePlayer(int playerId, Direction direction) {
        gameMap.movePlayer(playerId, direction);
    }

    //Will throw NullPointerException if the player does not exist on the map
    public void despawnPlayer(int playerId) {
        gameMap.despawnPlayer(playerId);
    }

    // Spawns the player at a random free position
    public void spawnPlayer(int playerId) {
        gameMap.spawnPlayer(playerId);
    }

}
