package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.game.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.game.io.PerformantByteArrayOutputStream;
import bg.sofia.uni.fmi.mjt.dungeons.game.state.GameState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

// StateDistributor distributes only the necessary data (PlayerPackage) to the clients
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
        for (var player : playerManager.getAllPlayers().entrySet()) {
            SocketChannel channel = player.getValue();
            byte[] playerPackageBytes = serializePlayerSegment(player.getKey());
            buffer.write(playerPackageBytes);
            try {
                buffer.writeIntoChannel(channel); // TODO check if it's necessary to reload every time
            } catch (IOException e) {
                System.out.println("Could not distribute the map to a player!");
                e.printStackTrace();
            }
        }
    }

    private byte[] serializePlayerSegment(int playerId) {
        PlayerSegment playerSegment = new PlayerSegment(gameState.gameMap(), gameState.getPlayerInfo(playerId));
        try (PerformantByteArrayOutputStream fastByteOutputStream = new PerformantByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fastByteOutputStream)) {
            playerSegment.writeExternal(objectOutputStream);
            objectOutputStream.flush(); // VERY IMPORTANT!!!
            return fastByteOutputStream.getBuf();
        } catch (IOException e) {
            throw new RuntimeException("Could not serialize game state", e);
        }
    }

}
