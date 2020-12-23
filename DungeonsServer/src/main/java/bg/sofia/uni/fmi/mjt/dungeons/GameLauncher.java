package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.game.Game;

public class GameLauncher {


    public static void main(String[] args) {
        Game game = new Game();
        game.start();
        //TODO make it into a console app that can be stopped manually
    }
}
