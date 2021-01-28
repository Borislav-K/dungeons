package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import java.io.IOException;
import java.nio.ByteBuffer;

// Transmissible objects can be properly (de)serialized for server<->client network transmission
public interface Transmissible {

    default void serialize(ByteBuffer out) throws IOException {

    }

    default void deserialize(ByteBuffer in) throws IOException {

    }
}
