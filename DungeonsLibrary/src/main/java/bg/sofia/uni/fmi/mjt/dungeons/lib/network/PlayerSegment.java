package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;

public interface PlayerSegment extends Transmissible {
    PlayerSegmentType type();
}
