package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Player implements Actor, Externalizable {

    private static final long serialVersionUID = 1L;

    private Position2D position;
    private int experience;
    private BattleStats battleStats;

    public Player(Position2D position, BattleStats battleStats) {
        this.position = position;
        this.battleStats = battleStats; // TODO probably going to have to clone it
    }

    public Player() {
    }

    Position2D getPosition() {
        return this.position;
    }

    void setPosition(Position2D position) {
        this.position = position;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(experience);
        out.writeObject(battleStats);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.experience = in.readInt();
        this.battleStats = (BattleStats) in.readObject();
    }
}
