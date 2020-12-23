package bg.sofia.uni.fmi.mjt.dungeons.game.action;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;
import bg.sofia.uni.fmi.mjt.dungeons.game.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.game.map.GameMap;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

public class PlayerActionHandler {

    private Queue<PlayerAction> actionQueue;
    private PlayerManager playerManager;
    private GameMap gameMap;

    public PlayerActionHandler(PlayerManager playerManager, GameMap gameMap) {
        this.playerManager = playerManager;
        this.gameMap = gameMap;
        this.actionQueue = new LinkedList<>();
    }


    public void publish(PlayerAction action) {
        actionQueue.add(action);
    }

    public void handleAll() {
        while (!actionQueue.isEmpty()) {
            handleAction(actionQueue.poll());
        }
    }

    private void handleAction(PlayerAction action) {
        switch (action.getType()) {
            case PLAYER_CONNECT -> handlePlayerConnect((PlayerConnect) action);
            case PLAYER_DISCONNECT -> handlePlayerDisconnect((PlayerDisconnect) action);
            case MOVEMENT -> handleMovement((PlayerMovement) action);
            default -> System.out.printf("Unknown event type %s\n", action.getType().toString());

        }
    }

    private void handlePlayerConnect(PlayerConnect connection) {
        try {
            int playerId = playerManager.addNewPlayer(connection.getChannel());
            gameMap.spawnPlayer(playerId);
        } catch (PlayerCapacityReachedException e) {
            System.out.println("Game is full - cannot add player"); //TODO let the player know as well
        }
    }

    private void handlePlayerDisconnect(PlayerDisconnect disconnection) {
        SocketChannel channel = disconnection.getChannel();
        try {
            channel.close();
        } catch (IOException e) {
            System.out.println("There was a problem when closing the player's channel");
            e.printStackTrace();
        }
        int playerId = playerManager.removePlayerByChannel(disconnection.getChannel());
        gameMap.despawnPlayer(playerId);
    }

    private void handleMovement(PlayerMovement action) {
        int playerId = playerManager.getPlayerIdByChannel(action.getInitiator());
        gameMap.movePlayer(playerId, action.getDirection());
    }

}
