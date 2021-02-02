package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchPlayerException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;
import bg.sofia.uni.fmi.mjt.dungeons.fight.Arena;
import bg.sofia.uni.fmi.mjt.dungeons.fight.FightResult;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.FightableActor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.ItemFactory;

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
        System.out.printf("Handling %s action\n", action.type());
        try {
            switch (action.type()) {
                case PLAYER_CONNECT -> handlePlayerConnect((PlayerConnect) action);
                case PLAYER_DISCONNECT -> handlePlayerDisconnect((PlayerDisconnect) action);
                case MOVEMENT -> handleMovement((PlayerMovement) action);
                case ATTACK -> handleAttack((PlayerAttack) action);
                case TREASURE_PICKUP -> handleTreasurePickup((TreasurePickup) action);
                case ITEM_USAGE -> handleItemUsage((ItemUsage) action);
                case ITEM_GRANT -> handleItemGrant((ItemGrant) action);
                case ITEM_THROW -> handleItemThrow((ItemThrow) action);
                default -> System.out.printf("Unknown action type %s\n", action.type().toString());
            }
        } catch (NoSuchPlayerException e) {
            System.out.println("The action was sent by a player that was removed. Closing channel...");
            closeChannel(action.initiator());
        }
    }

    private void handlePlayerConnect(PlayerConnect connection) {
        try {
            Player player = playerManager.createNewPlayer(connection.initiator());
            gameMap.spawnPlayer(player);
        } catch (PlayerCapacityReachedException e) {
            System.out.println("Game is full - cannot add player"); //TODO let the player know as well and add test for it
            // TODO probably close the channel - otherwise player segments need to be sent with a proper flag
        }
    }

    private void handlePlayerDisconnect(PlayerDisconnect disconnection) throws NoSuchPlayerException {
        SocketChannel playerChannel = disconnection.initiator();
        Player player = playerManager.getPlayerByChannel(playerChannel);
        playerManager.removePlayer(player);
        gameMap.despawnActor(player);
        try {
            playerChannel.close();
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

        Actor otherActor = player.position().getActorNotSameAs(player, ActorType.PLAYER, ActorType.MINION);
        if (otherActor == null) {
            return;
        }
        FightResult fightResult = Arena.makeActorsFight(player, (FightableActor) otherActor);
        handleFightResult(fightResult);
    }

    private void handleFightResult(FightResult fightResult) {
        FightableActor winner = fightResult.winner();
        FightableActor loser = fightResult.loser();
        gameMap.despawnActor(loser);
        if (winner.type().equals(ActorType.PLAYER)) {
            ((Player) winner).increaseXP(loser.XPReward());
        }
        if (loser.type().equals(ActorType.PLAYER)) {
            Player player = (Player) loser;
            player.sufferDeathPenalty();
            gameMap.spawnPlayer(player);
        }
    }

    private void handleTreasurePickup(TreasurePickup action) throws NoSuchPlayerException {
        Player player = playerManager.getPlayerByChannel(action.initiator());

        Actor treasureToPickup = player.position().getActorNotSameAs(player, ActorType.TREASURE);
        if (treasureToPickup != null) {
            gameMap.despawnActor(treasureToPickup);
            player.addItemToInventory(ItemFactory.random());
        }
    }

    private void handleItemUsage(ItemUsage action) throws NoSuchPlayerException {
        Player player = playerManager.getPlayerByChannel(action.initiator());
        player.useItemFromInventory(action.itemNumber());
    }

    private void handleItemGrant(ItemGrant action) throws NoSuchPlayerException {
        Player player = playerManager.getPlayerByChannel(action.initiator());
        Player receivingPlayer = (Player) player.position().getActorNotSameAs(player, ActorType.PLAYER);
        if (receivingPlayer != null) {
            giveItemTo(player, receivingPlayer, action.itemNumber());
        }
    }

    private void giveItemTo(Player sender, Player receiver, int itemNumber) {
        Item itemToGive = sender.removeItemFromInventory(itemNumber);
        if (itemToGive != null) {
            receiver.addItemToInventory(itemToGive);
        }
    }

    private void handleItemThrow(ItemThrow action) throws NoSuchPlayerException {
        Player player = playerManager.getPlayerByChannel(action.initiator());
        player.removeItemFromInventory(action.itemNumber());
    }

    private static void closeChannel(SocketChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            System.out.println("The channel has already been closed");
        }
    }
}
