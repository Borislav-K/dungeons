package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.ActorRepository;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// PlayerSegment contains the data relevant only for a single player
public class PlayerSegment {

    private PlayerSegmentType playerSegmentType;
    private int playerId;
    private ActorRepository actorRepository;

    public PlayerSegment() {
        this.actorRepository = new ActorRepository();
    }

    public PlayerSegment(int playerId, ActorRepository actorRepository) {
        this.playerId = playerId;
        this.actorRepository = actorRepository;
        this.playerSegmentType = PlayerSegmentType.INITIAL;
    }

    public int playerId() {
        return playerId;
    }

    public ActorRepository actorRepository() {
        return actorRepository;
    }


    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(playerSegmentType.ordinal());
        out.writeInt(playerId);
        actorRepository.serialize(out);
    }

    public void deserialize(DataInputStream in) throws IOException {
        playerSegmentType = PlayerSegmentType.values()[in.readInt()];
        playerId = in.readInt();
        actorRepository.deserialize(in);
    }
}
