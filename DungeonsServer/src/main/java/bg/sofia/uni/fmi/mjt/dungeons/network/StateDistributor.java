package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.game.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.game.io.PerformantByteArrayOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;

// StateDistributor distributes only the necessary data (PlayerSegment) to each player
public class StateDistributor {

    private GameMap gameMap;
    private PlayerManager playerManager;

    private SmartBuffer buffer;

    public StateDistributor(PlayerManager playerManager, GameMap gameMap) {
        this.gameMap = gameMap;
        this.playerManager = playerManager;
        this.buffer = new SmartBuffer();
    }

    public void distributeState() {
        for (Player player : playerManager.getAllPlayers()) {
            SocketChannel channel = player.channel();
            byte[] playerPackageBytes = serializePlayerSegment(player);
            buffer.write(playerPackageBytes);
            try {
                buffer.writeIntoChannel(channel);
            } catch (IOException e) {
                System.out.println("Could not distribute the map to a player!");
                //TODO should probably remove player here -> seems to happen when they close the channel
                e.printStackTrace();
            }
        }
    }

    private byte[] serializePlayerSegment(Player player) {
        PlayerSegment playerSegment = new PlayerSegment(gameMap, player);
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
