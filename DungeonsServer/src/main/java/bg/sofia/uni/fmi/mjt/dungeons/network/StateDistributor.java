package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.game.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.game.io.PerformantByteArrayOutputStream;
import bg.sofia.uni.fmi.mjt.dungeons.game.state.GameState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

// StateDistributor distributes only the necessary data (PlayerSegment) to each player
public class StateDistributor {

    private GameState gameState;
    private PlayerManager playerManager;

    private SmartBuffer buffer;

    public StateDistributor(PlayerManager playerManager, GameState gameState) {
        this.gameState = gameState;
        this.playerManager = playerManager;
        this.buffer = new SmartBuffer();
    }

    public void distributeState() {
        for (var playerEntry : playerManager.getAllPlayers().entrySet()) {
            SocketChannel channel = playerEntry.getValue();
            byte[] playerPackageBytes = serializePlayerSegment(playerEntry.getKey());
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

    private byte[] serializePlayerSegment(int playerId) {
        PlayerSegment playerSegment = new PlayerSegment(gameState.gameMap(), gameState.getPlayerInfo(playerId));
        try (var byteArrayOutputStream = new PerformantByteArrayOutputStream();
             var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            playerSegment.writeExternal(objectOutputStream);
            objectOutputStream.flush();
            return byteArrayOutputStream.getBuf();
        } catch (IOException e) {
            throw new RuntimeException("Could not serialize game state", e);
        }
    }

}
