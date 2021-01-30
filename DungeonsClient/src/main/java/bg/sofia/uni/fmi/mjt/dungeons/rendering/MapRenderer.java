package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.dungeons.lib.GameConfigurator.MAP_DIMENSIONS;
import static bg.sofia.uni.fmi.mjt.dungeons.lib.GameConfigurator.OBSTACLE_POSITIONS;

public class MapRenderer {
    private static final int MAP_FIELD_SIZE = 25;

    private static final ImageRepository imageRepository = ImageRepository.getInstance();

    public static void renderMap(Graphics2D g2d, Collection<Position2D> positionsWithActors) {
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Comic Sans", Font.PLAIN, MAP_FIELD_SIZE));

        drawObstacles(g2d);
        positionsWithActors.forEach(position -> drawPosition(g2d, position));
    }

    private static void drawObstacles(Graphics2D g2d) {
        for (int obstacle : OBSTACLE_POSITIONS) {
            int x = (obstacle / MAP_DIMENSIONS) * MAP_FIELD_SIZE;
            int y = (obstacle % MAP_DIMENSIONS) * MAP_FIELD_SIZE;
            g2d.drawImage(imageRepository.obstacleImage(), x, y, null);
        }
    }

    private static void drawPosition(Graphics2D g2d, Position2D pos) {
        List<Actor> actors = pos.actors();
        boolean isAloneActorOnPosition = actors.size() < 2;
        int x = pos.x() * MAP_FIELD_SIZE;
        int y = pos.y() * MAP_FIELD_SIZE;

        drawActor(g2d, actors.get(0), isAloneActorOnPosition, x, y);
        if (!isAloneActorOnPosition) {
            drawActor(g2d, actors.get(1), false, x + MAP_FIELD_SIZE / 2, y);
        }
    }

    private static void drawActor(Graphics2D g2d, Actor actor, boolean isAloneOnPosition, int x, int y) {
        BufferedImage imageToDraw = switch (actor.type()) {
            case TREASURE -> imageRepository.treasureImage();
            case MINION -> imageRepository.minionPictureForLevel(((Minion) actor).level());
            case PLAYER -> imageRepository.playerPictureForId(((Player) actor).id());
        };
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        if (!isAloneOnPosition) {
            at.scale(0.5, 1);
        }
        g2d.drawImage(imageToDraw, at, null);
    }
}
