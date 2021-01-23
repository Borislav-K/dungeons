package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchPlayerException;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.FightableActor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;
import bg.sofia.uni.fmi.mjt.dungeons.fight.Arena;
import bg.sofia.uni.fmi.mjt.dungeons.fight.FightResult;
import bg.sofia.uni.fmi.mjt.dungeons.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
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
        System.out.printf("Handling %s action\n", action.type());
        try {
            switch (action.type()) {
                case PLAYER_CONNECT -> handlePlayerConnect((PlayerConnect) action);
                case PLAYER_DISCONNECT -> handlePlayerDisconnect((PlayerDisconnect) action);
                case MOVEMENT -> handleMovement((PlayerMovement) action);
                case ATTACK -> handleAttack((PlayerAttack) action);
                default -> System.out.printf("Unknown event type %s\n", action.type().toString());
            }
        } catch (NoSuchPlayerException e) {
            System.out.println("The action was sent by a player that was removed from the player manager");
        }
    }

    private void handlePlayerConnect(PlayerConnect connection) {
        try {
            Player player = playerManager.createNewPlayer(connection.initiator());
            gameMap.spawnPlayer(player);
        } catch (PlayerCapacityReachedException e) {
            System.out.println("Game is full - cannot add player"); //TODO let the player know as well
        }
    }

    private void handlePlayerDisconnect(PlayerDisconnect disconnection) throws NoSuchPlayerException {
        SocketChannel channel = disconnection.initiator();
        Player player = playerManager.getPlayerByChannel(channel);
        playerManager.removePlayer(player);
        gameMap.despawnActor(player);
        try {
            channel.close();
        } catch (IOException e) {
            System.out.printf("There was a problem when closing the player %d's channel", player.id());
        }
    }

    private void handleMovement(PlayerMovement action) throws NoSuchPlayerException {
        Player player = playerManager.getPlayerByChannel(action.initiator());
        gameMap.movePlayer(player, action.direction());
    }

    private void handleAttack(PlayerAttack action) throws NoSuchPlayerException {
        Player player = playerManager.getPlayerByChannel(action.initiator());

        Position2D playerPosition = player.position();
        List<Actor> actorsAtPosition = playerPosition.actors();
        if (actorsAtPosition.size() != 2) {
            return;
        }
        Actor actor1 = actorsAtPosition.get(0);
        Actor actor2 = actorsAtPosition.get(1);
        if (actor1.type().equals(ActorType.TREASURE) || actor2.type().equals(ActorType.TREASURE)) {
            return;
        }
        FightResult fightResult;
        if (actor1.equals(player)) {
            fightResult = Arena.makeActorsFight(player, (FightableActor) actor2);
        } else {
            fightResult = Arena.makeActorsFight(player, (FightableActor) actor1);
        }

        handleFightResult(fightResult);
    }

    private void handleFightResult(FightResult fightResult) {
        FightableActor winner = fightResult.winner();
        FightableActor loser = fightResult.loser();
        gameMap.despawnActor(loser);
        if (winner.type().equals(ActorType.PLAYER)) {
            ((Player) winner).increaseXP(loser.XPReward());
        }
    }

}
