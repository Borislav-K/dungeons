package bg.sofia.uni.fmi.mjt.dungeons.lib;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.SmartBuffer;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.Transmissible;

import java.io.IOException;
import java.util.*;

public class Position2D implements Transmissible {
    private static final int MAX_ACTORS_AT_POSITION = 2;

    private int x;
    private int y;

    private List<Actor> actors;
    private boolean isObstaclePosition;

    public Position2D() {
        actors = new ArrayList<>();
    }

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

    /***
     * Returns an actor (or null if it does not exist) on this position,
     * that is of the given type and not same as the given actor
     *
     * @param actor - the actor used for the "not-same-as" relation
     * @param allowedTypes  - the desired actor types
     * @return
     */
    public Actor getActorNotSameAs(Actor actor, ActorType... allowedTypes) {
        if (actors.size() != MAX_ACTORS_AT_POSITION) {
            return null;
        }
        Actor actor1AtPosition = actors.get(0);
        Actor actor2AtPosition = actors.get(1);
        Actor candidateActor = actor1AtPosition.equals(actor) ? actor2AtPosition : actor1AtPosition;
        boolean isProperCandidate = Arrays.stream(allowedTypes).anyMatch(type -> type.equals(candidateActor.type()));
        return isProperCandidate ? candidateActor : null;
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

    // Only positions with actors will be (de)serialized
    @Override
    public void serialize(SmartBuffer out) throws IOException {
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(actors.size());
        for (Actor actor : actors) {
            out.writeInt(actor.type().ordinal());
            actor.serialize(out);
        }
    }

    @Override
    public void deserialize(SmartBuffer in) throws IOException {
        x = in.readInt();
        y = in.readInt();
        int actorsCount = in.readInt();
        for (int i = 1; i <= actorsCount; i++) {
            ActorType actorType = ActorType.values()[in.readInt()];
            Actor actor = switch (actorType) {
                case PLAYER -> new Player();
                case MINION -> new Minion();
                case TREASURE -> new Treasure();
            };
            actor.deserialize(in);
            actors.add(actor);
        }
    }
}
