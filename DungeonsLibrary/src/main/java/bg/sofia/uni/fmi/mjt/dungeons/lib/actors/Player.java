package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.LevelCalculator;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.exceptions.ItemNumberOutOfBoundsException;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.Inventory;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.*;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Player implements FightableActor {

    private static final int XP_REWARD_PER_PLAYER_LVL = 50;

    private static final int BASE_HEALTH = 150;
    private static final int BASE_MANA = 150;
    private static final int BASE_ATTACK = 40;
    private static final int BASE_DEFENSE = 10;

    private static final int HEALTH_GAIN_PER_LEVEL = 20;
    private static final int MANA_GAIN_PER_LEVEL = 20;
    private static final int ATTACK_GAIN_PER_LEVEL = 15;
    private static final int DEFENSE_GAIN_PER_LEVEL = 5;


    private static BattleStats playerStatsForLevel(int level) {
        return new BattleStats(BASE_HEALTH + HEALTH_GAIN_PER_LEVEL * level,
                BASE_MANA + MANA_GAIN_PER_LEVEL * level,
                BASE_ATTACK + ATTACK_GAIN_PER_LEVEL * level,
                BASE_DEFENSE + DEFENSE_GAIN_PER_LEVEL * level);
    }

    private int id;
    private SocketChannel channel;
    private int experience;
    private Position2D position;
    private BattleStats stats;
    private Inventory inventory;
    private Weapon weapon;
    private Spell spell;

    public Player() {
        this.experience = 0;
        this.stats = playerStatsForLevel(1);
        this.inventory = new Inventory();
    }

    public Player(int id, SocketChannel channel) {
        this();
        this.id = id;
        this.channel = channel;
    }

    public int id() {
        return id;
    }

    public SocketChannel channel() {
        return channel;
    }

    public int level() {
        return LevelCalculator.getLevelByExperience(experience);
    }

    public int XPPercentage() {
        return LevelCalculator.getPercentageToNextLevel(experience);
    }

    public boolean isDead() {
        return stats.currentHealth() == 0;
    }

    public Weapon weapon() {
        return weapon;
    }

    public void addItemToInventory(Item item) {
        inventory.addItemToInventory(item);
    }

    public void useItemFromInventory(int itemNumber) throws ItemNumberOutOfBoundsException {
        Item itemToUse = inventory.getItem(itemNumber);
        boolean itemUsed = switch (itemToUse.type()) {
            case HEALTH_POTION -> drinkHealthPotion((HealthPotion) itemToUse);
            case MANA_POTION -> drinkManaPotion((ManaPotion) itemToUse);
            case WEAPON -> equipWeapon((Weapon) itemToUse);
            case SPELL -> learnSpell((Spell) itemToUse);
        };
        if (itemUsed) {
            inventory.removeItem(itemNumber);
        }
    }

    private boolean drinkHealthPotion(HealthPotion potion) {
        stats.heal(potion.healingAmount());
        return true;
    }

    private boolean drinkManaPotion(ManaPotion potion) {
        stats.replenish(potion.replenishmentAmount());
        return true;
    }

    private boolean equipWeapon(Weapon weapon) {
        if (weapon.level() <= LevelCalculator.getLevelByExperience(experience)) {
            this.weapon = weapon;
            return true;
        }
        return false;
    }

    private boolean learnSpell(Spell spell) {
        if (spell.level() <= LevelCalculator.getLevelByExperience(experience)) {
            this.spell = spell;
            return true;
        }
        return false;
    }

    public Item removeItemFromInventory(int itemNumber) throws ItemNumberOutOfBoundsException {
        return inventory.removeItem(itemNumber);
    }

    @Override
    public BattleStats stats() {
        return stats;
    }

    @Override
    public Position2D position() {
        return this.position;
    }

    @Override
    public ActorType type() {
        return ActorType.PLAYER;
    }

    @Override
    public int XPReward() {
        return LevelCalculator.getLevelByExperience(experience) * XP_REWARD_PER_PLAYER_LVL;
    }

    public Inventory inventory() {
        return inventory;
    }

    public void setPosition(Position2D position) {
        this.position = position;
    }

    public void increaseXP(int amount) {
        int previousLevel = LevelCalculator.getLevelByExperience(experience);
        experience += amount;
        int currentLevel = LevelCalculator.getLevelByExperience(experience);
        if (currentLevel > previousLevel) {
            stats = playerStatsForLevel(currentLevel);
        }
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(id);
        out.writeInt(position.x());
        out.writeInt(position.y());
        out.writeInt(experience);
        stats.serialize(out);
        if (weapon == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            weapon.serialize(out);
        }
        if (spell == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            spell.serialize(out);
        }
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        id = in.readInt();
        position = new Position2D(in.readInt(), in.readInt());
        experience = in.readInt();
        stats.deserialize(in);
        boolean hasWeaponEquipped = in.readBoolean();
        if (hasWeaponEquipped) {
            weapon = new Weapon();
            weapon.deserialize(in);
        }
        boolean knowsSpell = in.readBoolean();
        if (knowsSpell) {
            spell = new Spell();
            spell.deserialize(in);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
