package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.game.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.GameMap;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class MapDistributor {

    private GameMap gameMap;
    private PlayerManager playerManager;

    private SmartBuffer buffer;

    public MapDistributor(PlayerManager playerManager, GameMap gameMap) {
        this.gameMap = gameMap;
        this.playerManager = playerManager;
        this.buffer = new SmartBuffer();
    }

    public void distributeMap() {
        byte[] mapBytes = gameMap.serialize();

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
