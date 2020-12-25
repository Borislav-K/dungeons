package bg.sofia.uni.fmi.mjt.dungeons.game.io;

import java.io.ByteArrayOutputStream;

public class PerformantByteArrayOutputStream extends ByteArrayOutputStream {
    public PerformantByteArrayOutputStream() {
    }

    public int getCount() {
        return count;
    }

    public byte[] getBuf() {
        return buf;
    }
}
