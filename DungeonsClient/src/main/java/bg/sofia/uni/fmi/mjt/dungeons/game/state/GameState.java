package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import java.io.Serializable;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private GameMap gameMap;

    public GameState() {
        this.gameMap = new GameMap();
    }

    public GameMap GameMap() {
        return gameMap;
    }

}
