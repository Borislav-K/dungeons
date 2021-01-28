package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import java.io.IOException;

// Transmissible objects can be properly (de)serialized for server<->client network transmission
public interface Transmissible {

    default void serialize(SmartBuffer out) throws IOException {

    }

    default void deserialize(SmartBuffer in) throws IOException {

    }
}
