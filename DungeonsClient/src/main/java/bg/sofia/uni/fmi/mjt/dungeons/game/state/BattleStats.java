package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BattleStats implements Externalizable {

    private static final long serialVersionUID = 1;

    private int health;
    private int mana;
    private int attack;
    private int defense;

    public BattleStats() {
    }

    @Override
    public void writeExternal(ObjectOutput out) {
        throw new UnsupportedOperationException("BattleStats should only be read from the server");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        this.health = in.readInt();
        this.mana = in.readInt();
        this.attack = in.readInt();
        this.defense = in.readInt();
    }

}
