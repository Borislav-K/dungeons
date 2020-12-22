package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.game.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.GameMap;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class MapDistributor {

    private GameMap gameMap;
    private PlayerManager playerManager;

    private SmartBuffer buffer;

    public MapDistributor(GameMap gameMap, PlayerManager playerManager) {
        this.gameMap = gameMap;
        this.playerManager = playerManager;
        this.buffer = new SmartBuffer();
    }

    public void distributeMap() throws IOException {
        byte[] mapBytes = gameMap.serialize();

        for (SocketChannel channel : playerManager.getAllPlayers()) {
            buffer.write(mapBytes);
            buffer.writeIntoChannel(channel); // TODO check if it's necessary to reload every time
        }
    }

}
