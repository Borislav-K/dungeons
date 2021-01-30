package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.Inventory;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class Player extends FightableActor {
    private static final int XP_REWARD_PER_PLAYER_LVL = 50;
    private static final int MAX_LEVEL = 10;
    private static final Map<Integer, Integer> REQUIRED_XP_FOR_LEVEL = Map.of(
            1, 0,
            2, 100,
            3, 200,
            4, 400,
            5, 650,
            6, 900,
            7, 1200,
            8, 1500,
            9, 2000,
            MAX_LEVEL, 3000
    );

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
        return REQUIRED_XP_FOR_LEVEL.entrySet().stream()
                .filter(entry -> entry.getValue() <= experience)
                .mapToInt(Map.Entry::getKey)
                .max().getAsInt();
    }

    public int XPPercentage() {
        int currentLevel = level();
        if (currentLevel == MAX_LEVEL) {
            return 0;
        }

        int levelXPGap = REQUIRED_XP_FOR_LEVEL.get(currentLevel + 1) - REQUIRED_XP_FOR_LEVEL.get(currentLevel);
        int currentLevelXP = experience - REQUIRED_XP_FOR_LEVEL.get(currentLevel);

        System.out.println(levelXPGap + " / " + currentLevelXP);
        double XPRatio = (double) currentLevelXP / levelXPGap * 100;
        return (int) XPRatio;
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
        int currentLevel = level();
        experience = REQUIRED_XP_FOR_LEVEL.get(currentLevel); // Reset XP gained for this level
        setPlayerStatsForLevel(currentLevel);
    }

    public void increaseXP(int amount) {
        int previousLevel = level();
        experience += amount;
        int currentLevel = level();
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
        return level() * XP_REWARD_PER_PLAYER_LVL;
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
        if (weapon.level() <= level()) {
            this.weapon = weapon;
            return true;
        }
        return false;
    }

    private boolean learnSpell(Spell spell) {
        if (spell.level() <= level()) {
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
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeByte(id);
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
        id = in.readByte();
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
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
