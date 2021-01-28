package bg.sofia.uni.fmi.mjt.dungeons.io;

import java.io.ByteArrayOutputStream;

// A wrapper around ByteArrayOutputStream which allows to get its buffer without making a copy of it every time
public class PerformantByteArrayOutputStream extends ByteArrayOutputStream {
    public PerformantByteArrayOutputStream() {
    }

    public byte[] getBuf() {
        return buf;
    }
}
