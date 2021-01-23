package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// Transmissible objects can be properly (de)serialized for server<->client network transmission
public interface Transmissible {

    default void serialize(DataOutputStream out) throws IOException {

    }

    default void deserialize(DataInputStream in) throws IOException {

    }
}
