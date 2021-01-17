package bg.sofia.uni.fmi.mjt.dungeons;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SmartBuffer {
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    private ByteBuffer buffer;

    public SmartBuffer() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public SmartBuffer(int capacity) {
        this.buffer = ByteBuffer.allocate(capacity); // TODO think about allocateDirect()
    }

    public byte[] read() {
        buffer.flip();
        byte[] destination = new byte[buffer.remaining()];
        buffer.get(destination);
        return destination;
    }

    public byte[] read(int limit) {
        buffer.flip();
        byte[] destination = new byte[limit];
        buffer.get(destination, 0, limit);
        return destination;
    }

    public void write(String s) {
        buffer.clear();
        buffer.put(s.getBytes());
    }

    public int readFromChannel(SocketChannel from) throws IOException {
        buffer.clear();
        return from.read(buffer);
    }

    public void writeIntoChannel(SocketChannel to) throws IOException {
        buffer.flip();
        to.write(buffer);
    }
}
