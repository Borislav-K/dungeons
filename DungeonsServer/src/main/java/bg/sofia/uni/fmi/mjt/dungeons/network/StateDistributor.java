package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.game.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.game.state.GameState;

import java.io.IOException;
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

    public void distributeMap() {
        byte[] mapBytes = gameState.serialize();

        for (SocketChannel channel : playerManager.getAllPlayers()) {
            buffer.write(mapBytes);
            try {
                buffer.writeIntoChannel(channel); // TODO check if it's necessary to reload every time
            } catch (IOException e) {
                System.out.println("Could not distribute the map to a player!");
                e.printStackTrace();
            }
        }
    }

}
