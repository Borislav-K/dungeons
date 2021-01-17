package bg.sofia.uni.fmi.mjt.dungeons.network;


import bg.sofia.uni.fmi.mjt.dungeons.actors.PlayerData;
import bg.sofia.uni.fmi.mjt.dungeons.game.GameMap;

import java.io.DataInputStream;
import java.io.IOException;


public class PlayerSegment {
    private GameMap gameMap;
    private PlayerData playerData;

    public PlayerSegment() {
        this.gameMap = new GameMap();
        this.playerData = new PlayerData();
    }

    public GameMap GameMap() {
        return gameMap;
    }

    public PlayerData playerData() {
        return playerData;
    }

    public void deserialize(DataInputStream in) throws IOException {
        in.readInt();
        gameMap.deserialize(in);
        playerData.deserialize(in);
    }

}
