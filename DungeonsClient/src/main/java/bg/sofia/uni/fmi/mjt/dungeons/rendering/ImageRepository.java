package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageRepository {

    private static final Path resourcesDir = Path.of("DungeonsClient", "src", "main", "resources");

    private static final ImageRepository instance = new ImageRepository();

    private BufferedImage attackPointsIcon;
    private BufferedImage defensePointsIcon;
    private BufferedImage obstacleImage;
    private BufferedImage treasureImage;
    private BufferedImage healthPotionImage;
    private BufferedImage manaPotionImage;
    private List<BufferedImage> minionPictures;
    private List<BufferedImage> playerPictures;
    private Map<Integer, BufferedImage> weaponPictures;
    private Map<Integer, BufferedImage> spellPictures;

    public static ImageRepository getInstance() {
        return instance;
    }

    private ImageRepository() {
        minionPictures = new ArrayList<>();
        playerPictures = new ArrayList<>();
        weaponPictures = new HashMap<>();
        spellPictures = new HashMap<>();
        try {
            attackPointsIcon = getImageByName("attack_points_icon.bmp");
            defensePointsIcon = getImageByName("defense_points_icon.bmp");
            obstacleImage = getImageByName("obstacle.bmp");
            treasureImage = getImageByName("treasure.bmp");
            healthPotionImage = getImageByName("health_potion.bmp");
            manaPotionImage = getImageByName("mana_potion.bmp");
            for (int i = 1; i <= 5; i++) {
                minionPictures.add(getImageByName("minion_level%d.bmp".formatted(i)));
            }
            for (int i = 1; i <= 9; i++) {
                playerPictures.add(getImageByName("player%d.bmp".formatted(i)));
            }

            weaponPictures.put(3, getImageByName("weapon_level3.bmp"));
            weaponPictures.put(5, getImageByName("weapon_level5.bmp"));

            spellPictures.put(2, getImageByName("spell_level2.bmp"));
            spellPictures.put(5, getImageByName("spell_level5.bmp"));
            spellPictures.put(8, getImageByName("spell_level8.bmp"));
        } catch (IOException e) {
            throw new IllegalStateException("Could not load a resource", e);
        }
    }

    public BufferedImage obstacleImage() {
        return obstacleImage;
    }

    public BufferedImage attackPointsIcon() {
        return attackPointsIcon;
    }

    public BufferedImage defensePointsIcon() {
        return defensePointsIcon;
    }

    public BufferedImage treasureImage() {
        return treasureImage;
    }

    public BufferedImage healthPotionImage() {
        return healthPotionImage;
    }

    public BufferedImage manaPotionImage() {
        return manaPotionImage;
    }

    public BufferedImage minionPictureForLevel(int level) {
        return minionPictures.get(level - 1);
    }

    public BufferedImage playerPictureForId(int id) {
        return playerPictures.get(id - 1);
    }

    public BufferedImage weaponPictureForLevel(int level) {
        return weaponPictures.get(level);
    }

    public BufferedImage spellPictureForLevel(int level) {
        return spellPictures.get(level);
    }

    private static BufferedImage getImageByName(String resourceName) throws IOException {
        return ImageIO.read(resourcesDir.resolve(resourceName).toFile());
    }
}
