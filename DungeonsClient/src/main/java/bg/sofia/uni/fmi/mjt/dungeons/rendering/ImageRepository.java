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

    private static final int PLAYER_PICTURES_COUNT = 9;
    private static final int MINION_PICTURES_COUNT = 5;
    private static final int[] VALID_SPELL_LEVELS = {2, 5, 8};
    private static final int[] VALID_WEAPON_LEVELS = {3, 5};

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

    private ImageRepository() {
        try {
            loadMiscellaneousPictures();
            loadMinionPictures();
            loadPlayerPictures();
            loadWeaponPictures();
            loadSpellPictures();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load a resource", e);
        }
    }

    public static ImageRepository getInstance() {
        return instance;
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

    private void loadMinionPictures() throws IOException {
        minionPictures = new ArrayList<>();
        for (int i = 1; i <= MINION_PICTURES_COUNT; i++) {
            minionPictures.add(getImageByName("minion_level%d.bmp".formatted(i)));
        }
    }

    private void loadPlayerPictures() throws IOException {
        playerPictures = new ArrayList<>();
        for (int i = 1; i <= PLAYER_PICTURES_COUNT; i++) {
            playerPictures.add(getImageByName("player%d.bmp".formatted(i)));
        }
    }

    private void loadWeaponPictures() throws IOException {
        weaponPictures = new HashMap<>();
        for (int i : VALID_WEAPON_LEVELS) {
            weaponPictures.put(i, getImageByName("weapon_level%d.bmp".formatted(i)));
        }
    }

    private void loadSpellPictures() throws IOException {
        spellPictures = new HashMap<>();
        for (int i : VALID_SPELL_LEVELS) {
            spellPictures.put(i, getImageByName("spell_level%d.bmp".formatted(i)));
        }
    }

    private void loadMiscellaneousPictures() throws IOException {
        attackPointsIcon = getImageByName("attack_points_icon.bmp");
        defensePointsIcon = getImageByName("defense_points_icon.bmp");
        obstacleImage = getImageByName("obstacle.bmp");
        treasureImage = getImageByName("treasure.bmp");
        healthPotionImage = getImageByName("health_potion.bmp");
        manaPotionImage = getImageByName("mana_potion.bmp");
    }

    private static BufferedImage getImageByName(String resourceName) throws IOException {
        return ImageIO.read(resourcesDir.resolve(resourceName).toFile());
    }
}
