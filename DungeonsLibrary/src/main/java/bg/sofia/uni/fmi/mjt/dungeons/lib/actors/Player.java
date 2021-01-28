package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.LevelCalculator;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.Inventory;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.*;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.SmartBuffer;

import java.io.IOException;

public class Player extends FightableActor {

    private static final int XP_REWARD_PER_PLAYER_LVL = 50;

    private static final int BASE_HEALTH = 170;
    private static final int BASE_MANA = 170;
    private static final int BASE_ATTACK = 55;
    private static final int BASE_DEFENSE = 15;

    private static final int HEALTH_GAIN_PER_LEVEL = 15;
    private static final int MANA_GAIN_PER_LEVEL = 15;
    private static final int ATTACK_GAIN_PER_LEVEL = 10;
    private static final int DEFENSE_GAIN_PER_LEVEL = 5;


    private void setPlayerStatsForLevel(int level) {
        setStats(BASE_HEALTH + HEALTH_GAIN_PER_LEVEL * (level - 1),
                BASE_MANA + MANA_GAIN_PER_LEVEL * (level - 1),
                BASE_ATTACK + ATTACK_GAIN_PER_LEVEL * (level - 1),
                BASE_DEFENSE + DEFENSE_GAIN_PER_LEVEL * (level - 1));
    }

    private int id;
    private int experience;
    private Inventory inventory;
    private Weapon weapon;
    private Spell spell;

    public Player() {
        this.inventory = new Inventory();
    }

    public Player(int id) {
        super(BASE_HEALTH, BASE_MANA, BASE_ATTACK, BASE_DEFENSE);
        this.id = id;
        this.experience = 0;
        this.inventory = new Inventory();
    }

    public int id() {
        return id;
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

    public Spell spell() {
        return spell;
    }

    public Inventory inventory() {
        return inventory;
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

    public Item removeItemFromInventory(int itemNumber) {
        return inventory.removeItem(itemNumber);
    }

    public void sufferDeathPenalty() {
        inventory.removeRandomItem();
        int currentLevel = LevelCalculator.getLevelByExperience(experience);
        experience = LevelCalculator.REQUIRED_XP_FOR_LEVEL.get(currentLevel); // Reset XP gained for this level
        setPlayerStatsForLevel(currentLevel);
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

    private boolean drinkHealthPotion(HealthPotion potion) {
        heal(potion.healingAmount());
        return true;
    }

    private boolean drinkManaPotion(ManaPotion potion) {
        replenish(potion.replenishmentAmount());
        return true;
    }

    @Override
    public void serialize(SmartBuffer out) throws IOException {
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
    public void deserialize(SmartBuffer in) throws IOException {
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

}
