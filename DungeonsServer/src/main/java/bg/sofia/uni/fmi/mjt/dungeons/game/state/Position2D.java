package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActorType;

import javax.print.event.PrintJobAttributeListener;
import java.util.HashSet;
import java.util.Set;

public class Position2D {

    /**
     * <h1> MAP SERIALIZATION FORMAT </h1>
     * The game map is a 20x20 array of bytes. Each byte represents the actors on the position [i][j]
     * There can be at most 2 actors on the same position - 2 players, player+minion, and player+treasure
     * A byte with value 0 means that there aren't any actors at this position
     * A byte with value 1 means that there is an obstacle at this position
     * A byte with value 2 means that there is a treasure at this position
     * A byte with value 3 means that there is a minion at this position
     * A byte with value 4 means that there is a BOSS enemy at this position
     * <h2>2 PLAYERS</h2>
     * Bytes 12-98 are reserved for 2 players and player+treasure:
     * x/10 is the ID of the first player, and x%10 is the ID of the second player(where x is the byte)
     * Bytes 10,20,...,90 are reserved to represent that there is only 1 player on this location(with ID x/10)
     * <h2>PLAYER+TREASURE</h2>
     * Since the treasure's type is randomly generated upon its pickup, the player+treasure positions are marked
     * with the bytes xx, where x/10(or x%10) is the ID of the player who is on the position
     * <h2>PLAYER+MINION</h2>
     * There are 2 types of minions - ordinary minions and bosses.
     * Bytes 10x mean that there is a weak minion + a player on this position, where X is the player's ID
     * Bytes 11x mean that there is a BOSS + a player on this position, where X is the player's ID
     */

    private static final byte EMPTY_SPACE_BYTE = 0;
    private static final byte TREASURE_BYTE = 2;
    private static final byte MINION_BYTE = 3;
    private static final byte BOSS_BYTE = 4;


    private static final int MAX_ACTORS_AT_POSITION = 2;

    private int x;
    private int y;

    private Set<Actor> actors;
    private boolean isObstaclePosition;

    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
        this.isObstaclePosition = false;
        this.actors = new HashSet<>(MAX_ACTORS_AT_POSITION);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void markAsObstacle() {
        isObstaclePosition = true;
    }

    public void addActor(Actor actor) {
        if (isObstaclePosition) {
            throw new RuntimeException(""); //TODO
        }
        if (actors.size() == MAX_ACTORS_AT_POSITION) {
            throw new RuntimeException(""); //TODO make a custom exception
        }
        actors.add(actor);
    }

    public void removeActor(Actor actor) {
        if (!actors.remove(actor)) {
            throw new RuntimeException(""); //TODO make a custom exception
        }
    }

    public boolean containsFreeSpace() {
        return !isObstaclePosition && actors.size() < MAX_ACTORS_AT_POSITION;
    }

    public boolean isSpawnable() {
        return !isObstaclePosition && actors.isEmpty();
    }

    //Returns the loser player (if there is such), null otherwise
    public Player makeActorsFight() {
        if (isObstaclePosition || actors.size() != MAX_ACTORS_AT_POSITION) {
            return null;
        }
        var actorsIterator = actors.iterator();
        Actor actor1 = actorsIterator.next();
        Actor actor2 = actorsIterator.next();
        if (actor1.getType().equals(ActorType.PLAYER) && actor2.getType().equals(ActorType.PLAYER)) {
            //TODO impl
            return null;
        }
        Player player;
        Minion minion;
        if (actor1.getType().equals(ActorType.PLAYER)) {
            if (!actor2.getType().equals(ActorType.MINION)) {
                return null;
            }
            player = (Player) actor1;
            minion = (Minion) actor2;
        } else {
            if (!actor1.getType().equals(ActorType.MINION)) {
                return null;
            }
            player = (Player) actor2;
            minion = (Minion) actor1;
        }
        player.increaseXP(minion.getXPReward());
        removeActor(minion);
        return null;
    }

    public byte toByte() {
        if (isObstaclePosition) {
            return 1;
        }
        if (actors.isEmpty()) {
            return EMPTY_SPACE_BYTE;
        }
        if (actors.size() == 1) {
            Actor actor = actors.stream().findFirst().get();
            return switch (actor.getType()) {
                case TREASURE -> TREASURE_BYTE;
                case MINION -> MINION_BYTE;
                case BOSS -> BOSS_BYTE;
                case PLAYER -> (byte) (getPlayerID(actor) * 10);
            };
        }
        var actorsIterator = actors.iterator();
        Actor actor1 = actorsIterator.next();
        Actor actor2 = actorsIterator.next();
        // One of the actors is a player for sure

        // Two players case
        if (actor1.getType().equals(ActorType.PLAYER) && actor2.getType().equals(ActorType.PLAYER)) {
            return (byte) (getPlayerID(actor1) * 10 + getPlayerID(actor2));
        }

        // One player case
        if (actor2.getType().equals(ActorType.PLAYER)) {
            return determineBytePlayerActor(actor2, actor1);
        } else {
            return determineBytePlayerActor(actor1, actor2);
        }
    }

    private byte determineBytePlayerActor(Actor player, Actor otherActor) {
        int playerId = getPlayerID(player);
        return switch (otherActor.getType()) {
            case TREASURE -> (byte) (playerId * 11);
            case MINION -> (byte) (playerId + 100);
            case BOSS -> (byte) (playerId + 110);
            default -> throw new IllegalArgumentException("Invalid actor type");
        };
    }

    private int getPlayerID(Actor actor) {
        return ((Player) actor).id();
    }
}
