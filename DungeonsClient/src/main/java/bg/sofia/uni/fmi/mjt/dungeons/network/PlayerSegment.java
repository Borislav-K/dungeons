package bg.sofia.uni.fmi.mjt.dungeons.network;


import bg.sofia.uni.fmi.mjt.dungeons.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.GameMap;

import java.io.DataInputStream;
import java.io.IOException;


public class PlayerSegment {
    private GameMap gameMap;
    private Player playerData;

    public PlayerSegment() {
        this.gameMap = new GameMap();
        this.playerData = new Player();
    }

    public GameMap GameMap() {
        return gameMap;
    }

    public Player playerData() {
        return playerData;
    }

    public void deserialize(DataInputStream in) throws IOException {
        in.readInt();
        gameMap.deserialize(in);
        playerData.deserialize(in);
    }

}
