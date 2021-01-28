package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.SmartBuffer;

import java.io.IOException;

// StateDistributor distributes only the necessary data (PlayerSegment) to each player
public class StateDistributor {

    private PlayerManager playerManager;
    private GameMap gameMap;

    private SmartBuffer buffer;

    public StateDistributor(PlayerManager playerManager, GameMap gameMap) {
        this.playerManager = playerManager;
        this.gameMap = gameMap;
        this.buffer = new SmartBuffer();
    }

    public void distributeState() {
        for (Player player : playerManager.getAllPlayers()) {
            serializePlayerSegmentIntoBuffer(player);
            try {
                buffer.writeIntoChannel(playerManager.getPlayerChannel(player));
            } catch (IOException e) {
                System.out.println("Could not distribute the map to a player!");
                gameMap.despawnActor(player);
                playerManager.removePlayer(player);
            }
        }
    }

    private void serializePlayerSegmentIntoBuffer(Player player) {
        try {
            buffer.startSerialization();
            PlayerSegment playerSegment = new PlayerSegment(player, gameMap.getPositionsWithActors());
            playerSegment.serialize(buffer.underlyingBuffer());
        } catch (IOException e) {
            throw new RuntimeException("Could not serialize game state", e);
        }
    }

}
