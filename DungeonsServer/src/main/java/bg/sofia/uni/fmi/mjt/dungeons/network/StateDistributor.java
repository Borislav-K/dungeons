package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.io.PerformantByteArrayOutputStream;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.SmartBuffer;

import java.io.DataOutputStream;
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
            byte[] playerPackageBytes = serializePlayerSegment(player);
            buffer.write(playerPackageBytes);
            try {
                buffer.writeIntoChannel(playerManager.getPlayerChannel(player));
            } catch (IOException e) {
                System.out.println("Could not distribute the map to a player!");
                gameMap.despawnActor(player);
                playerManager.removePlayer(player);
            }
        }
    }

    private byte[] serializePlayerSegment(Player player) {
        PlayerSegment playerSegment = new PlayerSegment(player, gameMap.getPositionsWithActors());
        try (var byteArrayOutputStream = new PerformantByteArrayOutputStream();
             var dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
            playerSegment.serialize(dataOutputStream);
            dataOutputStream.flush();
            return byteArrayOutputStream.getBuf();
        } catch (IOException e) {
            throw new RuntimeException("Could not serialize game state", e);
        }
    }

}
