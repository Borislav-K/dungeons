package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.game.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.actors.Player;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

// PlayerSegment contains the data relevant only for a single player
public class PlayerSegment implements Externalizable {

    private GameMap gameMap;
    private Player playerData;

    public PlayerSegment() {
    }

    public PlayerSegment(GameMap gameMap, Player playerData) {
        this.gameMap = gameMap;
        this.playerData = playerData;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(gameMap);
        out.writeObject(playerData);
    }

    @Override
    public void readExternal(ObjectInput in) {
        throw new UnsupportedOperationException("");
    }
}
