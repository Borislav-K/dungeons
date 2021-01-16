package bg.sofia.uni.fmi.mjt.dungeons.game;

import bg.sofia.uni.fmi.mjt.dungeons.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.enums.ActorType;

import java.util.ArrayList;
import java.util.List;

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
     * Bytes 10x mean that there is a minion + a player on this position, where X is the player's ID
     */

    private static final byte EMPTY_SPACE_BYTE = 0;
    private static final byte TREASURE_BYTE = 2;
    private static final byte MINION_BYTE = 3;


    private static final int MAX_ACTORS_AT_POSITION = 2;

    private int x;
    private int y;

    private List<Actor> actors;
    private boolean isObstaclePosition;

    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
        this.actors = new ArrayList<>(MAX_ACTORS_AT_POSITION);
        this.isObstaclePosition = false;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public List<Actor> actors() {
        return actors;
    }

    public void markAsObstacle() {
        isObstaclePosition = true;
    }

    public void addActor(Actor actor) {
        if (!isObstaclePosition && actors.size() < MAX_ACTORS_AT_POSITION) {
            actors.add(actor);
        }
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
    }

    public boolean containsFreeSpace() {
        return !isObstaclePosition && actors.size() < MAX_ACTORS_AT_POSITION;
    }

    public boolean isSpawnable() {
        return !isObstaclePosition && actors.isEmpty();
    }

    public byte toByte() {
        if (isObstaclePosition) {
            return 1;
        }
        if (actors.isEmpty()) {
            return EMPTY_SPACE_BYTE;
        }
        if (actors.size() == 1) {
            Actor actor = actors.get(0);
            return switch (actor.type()) {
                case TREASURE -> TREASURE_BYTE;
                case MINION -> MINION_BYTE;
                case PLAYER -> (byte) (getPlayerID(actor) * 10);
            };
        }
        Actor actor1 = actors.get(0);
        Actor actor2 = actors.get(1);
        // One of the actors is a player for sure

        // Two players case
        if (actor1.type().equals(ActorType.PLAYER) && actor2.type().equals(ActorType.PLAYER)) {
            return (byte) (getPlayerID(actor1) * 10 + getPlayerID(actor2));
        }

        // One player case
        if (actor2.type().equals(ActorType.PLAYER)) {
            return determineBytePlayerActor(actor2, actor1);
        } else {
            return determineBytePlayerActor(actor1, actor2);
        }
    }

    private byte determineBytePlayerActor(Actor player, Actor otherActor) {
        int playerId = getPlayerID(player);
        return switch (otherActor.type()) {
            case TREASURE -> (byte) (playerId * 11);
            case MINION -> (byte) (playerId + 100);
            default -> throw new IllegalArgumentException("Invalid actor type");
        };
    }

    private int getPlayerID(Actor actor) {
        return ((Player) actor).id();
    }
}
