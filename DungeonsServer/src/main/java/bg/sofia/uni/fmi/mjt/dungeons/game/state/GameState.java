package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;
import bg.sofia.uni.fmi.mjt.dungeons.game.io.PerformantByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Game state holds the current state of the game with all the information inside
public class GameState {

    private GameMap gameMap;
    private transient Map<Integer, Player> players;

    public GameState() {
        this.players = new HashMap<>();
        this.gameMap = new GameMap(players);
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

    public GameMap gameMap() {
        return gameMap;
    }

}
