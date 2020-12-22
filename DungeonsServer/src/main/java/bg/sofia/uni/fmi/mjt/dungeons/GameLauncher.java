package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.game.Game;

public class GameLauncher {

    private static final int SERVER_PORT = 10_000;

    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();
        game.start();
        //TODO make it into a console app that can be stopped manually
    }
}
