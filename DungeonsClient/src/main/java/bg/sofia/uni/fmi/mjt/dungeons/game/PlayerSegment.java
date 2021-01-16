package bg.sofia.uni.fmi.mjt.dungeons.game;


import bg.sofia.uni.fmi.mjt.dungeons.actors.Player;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class PlayerSegment implements Externalizable {

    private GameMap gameMap;
    private Player playerData;

    public PlayerSegment() {
        this.gameMap = new GameMap();
    }

    public GameMap GameMap() {
        return gameMap;
    }

    public Player playerData() {
        return playerData;
    }

    @Override
    public void writeExternal(ObjectOutput out) {
        throw new UnsupportedOperationException("Player segments should only be read from the server");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.gameMap = (GameMap) in.readObject();
        this.playerData = (Player) in.readObject();
    }

}
