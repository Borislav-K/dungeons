package bg.sofia.uni.fmi.mjt.dungeons.game;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;
import bg.sofia.uni.fmi.mjt.dungeons.game.event.*;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.network.GameServer;
import bg.sofia.uni.fmi.mjt.dungeons.network.MapDistributor;
import bg.sofia.uni.fmi.mjt.dungeons.network.SmartBuffer;

import java.io.IOException;
import java.nio.channels.SocketChannel;


public class Game {

    private static final double FRAME_NANOS = 17000000.0;

    private ActionQueue actionQueue;
    private GameServer server;
    private GameMap gameMap;
    private PlayerManager playerManager;
    private MapDistributor mapDistributor;

    public Game() {
        this.actionQueue = new ActionQueue();
        this.server = new GameServer(actionQueue);
        this.gameMap = new GameMap();
        this.playerManager = new PlayerManager();
        this.mapDistributor = new MapDistributor(gameMap, playerManager);
    }

    public void start() {
        server.start();
        startLoop();
    }

    private void startLoop() {
        double framesElapsed = 0;
        long lastMoment = System.nanoTime();
        while (true) {
            long now = System.nanoTime();
            framesElapsed += (now - lastMoment) / FRAME_NANOS;
            lastMoment = now;
            while (framesElapsed >= 1) { // Catch up with the frames if there was a delay
                tick();
                framesElapsed--;
                renderMap(gameMap.getMap());//TODO remove after development is done
            }

        }
    }


    public void renderMap(char[][] map) { //TODO remove
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

    private void tick() {
        server.fetchPlayerActions();
        while (actionQueue.hasEvents()) {
            handleEvent(actionQueue.getNext());
        }
        try {
            mapDistributor.distributeMap();
        } catch (IOException e) {
            throw new IllegalStateException(e); //TODO improve
        }
    }

    private void handleEvent(PlayerAction playerAction) { // TODO make a dedicated handler for this
        switch (playerAction.getType()) {
            case MOVEMENT -> handleMovement((PlayerMovement) playerAction); // TODO think of how to go around casts
            case PLAYER_CONNECT -> handlePlayerConnection((PlayerConnect) playerAction);
            case PLAYER_DISCONNECT -> handlePlayerDisconnection((PlayerDisconnect) playerAction);
            default -> System.out.printf("Unknown event type %s\n", playerAction.getType().toString());
        }
    }

    private void handlePlayerConnection(PlayerConnect connection) {
        try {
            int playerId = playerManager.addNewPlayer(connection.getChannel());
            gameMap.spawnPlayer(playerId);
        } catch (PlayerCapacityReachedException e) {
            System.out.println("Game is full - cannot add player"); //TODO let the player know as well
        }
    }

    private void handlePlayerDisconnection(PlayerDisconnect disconnection) {
        SocketChannel channel = disconnection.getChannel();
        try {
            channel.close();
        } catch (IOException e) {
            System.out.println("There was a problem when closing the player's channel");
            e.printStackTrace();
        }
        int playerId = playerManager.removePlayerByChannel(disconnection.getChannel());
        gameMap.removePlayer(playerId);
    }

    private void handleMovement(PlayerMovement movement) {
        int playerId = playerManager.getPlayerIdByChannel(movement.getInitiator());
        gameMap.movePlayer(playerId, movement.getDirection());
    }

}
