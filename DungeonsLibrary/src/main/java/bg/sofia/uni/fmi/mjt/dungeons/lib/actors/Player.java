package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.LevelCalculator;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.Inventory;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Player extends FightableActor {

    private static final int XP_REWARD_PER_PLAYER_LVL = 50;

    private static final int BASE_HEALTH = 150;
    private static final int BASE_MANA = 150;
    private static final int BASE_ATTACK = 40;
    private static final int BASE_DEFENSE = 10;

    private static final int HEALTH_GAIN_PER_LEVEL = 20;
    private static final int MANA_GAIN_PER_LEVEL = 20;
    private static final int ATTACK_GAIN_PER_LEVEL = 15;
    private static final int DEFENSE_GAIN_PER_LEVEL = 5;


    private void setPlayerStatsForLevel(int level) {
        setStats(BASE_HEALTH + HEALTH_GAIN_PER_LEVEL * level,
                BASE_MANA + MANA_GAIN_PER_LEVEL * level,
                BASE_ATTACK + ATTACK_GAIN_PER_LEVEL * level,
                BASE_DEFENSE + DEFENSE_GAIN_PER_LEVEL * level);
    }

    private int id;
    private transient SocketChannel channel;
    private int experience;
    private Inventory inventory;
    private Weapon weapon;
    private Spell spell;

    public Player() {
        super(BASE_HEALTH, BASE_MANA, BASE_ATTACK, BASE_DEFENSE);
        this.experience = 0;
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

    public Weapon weapon() {
        return weapon;
    }

    public void addItemToInventory(Item item) {
        inventory.addItemToInventory(item);
    }

    public void useItemFromInventory(int itemNumber) {
        Item itemToUse = inventory.getItem(itemNumber);
        if (itemToUse == null) {
            return;
        }
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
        heal(potion.healingAmount());
        return true;
    }

    private boolean drinkManaPotion(ManaPotion potion) {
        replenish(potion.replenishmentAmount());
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

    public Item removeItemFromInventory(int itemNumber) {
        return inventory.removeItem(itemNumber);
    }

    public void sufferDeathPenalty() {
        inventory.removeRandomItem();
        int currentLevel = LevelCalculator.getLevelByExperience(experience);
        experience = LevelCalculator.REQUIRED_XP_FOR_LEVEL.get(currentLevel); // Reset XP gained for this level
        setPlayerStatsForLevel(currentLevel);
    }

    @Override
    public ActorType type() {
        return ActorType.PLAYER;
    }

    @Override
    public int XPReward() {
        return LevelCalculator.getLevelByExperience(experience) * XP_REWARD_PER_PLAYER_LVL;
    }

    @Override
    public void dealDamage(FightableActor subject) {
        int spellDamage = spell == null ? 0 : spell.damage();
        int weaponDamage = weapon == null ? 0 : weapon.attack();

        if (spellDamage > weaponDamage && currentMana >= spell.manaCost()) {
            drainMana(spell.manaCost());
        }
        subject.takeDamage(attack() + Math.max(spellDamage, weaponDamage));
    }

    public Spell spell() {
        return spell;
    }

    public Inventory inventory() {
        return inventory;
    }

    public void increaseXP(int amount) {
        int previousLevel = LevelCalculator.getLevelByExperience(experience);
        experience += amount;
        int currentLevel = LevelCalculator.getLevelByExperience(experience);
        if (currentLevel > previousLevel) {
            setPlayerStatsForLevel(currentLevel);
        }
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeInt(id);
        out.writeInt(experience);
        out.writeBoolean(weapon != null);
        if (weapon != null) {
            weapon.serialize(out);
        }
        out.writeBoolean(spell != null);
        if (spell != null) {
            spell.serialize(out);
        }
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        super.deserialize(in);
        id = in.readInt();
        experience = in.readInt();
        if (in.readBoolean()) {
            weapon = new Weapon();
            weapon.deserialize(in);
        }
        if (in.readBoolean()) {
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
