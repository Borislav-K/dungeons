package bg.sofia.uni.fmi.mjt.dungeons.lib.position;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position2D {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position2D that = (Position2D) o;
        return x == that.x &&
               y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position2D{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
}
