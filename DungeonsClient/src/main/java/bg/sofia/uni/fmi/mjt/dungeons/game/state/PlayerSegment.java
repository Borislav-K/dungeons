package bg.sofia.uni.fmi.mjt.dungeons.game.state;


import java.io.*;

public class PlayerSegment implements Externalizable {

    private GameMap gameMap;
    private Player playerData;

    public PlayerSegment() {
        this.gameMap = new GameMap();
    }

    public GameMap GameMap() {
        return gameMap;
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
