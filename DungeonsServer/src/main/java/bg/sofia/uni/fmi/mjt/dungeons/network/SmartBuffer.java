package bg.sofia.uni.fmi.mjt.dungeons.network;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SmartBuffer {
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private ByteBuffer buffer;

    public SmartBuffer() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public SmartBuffer(int capacity) {
        this.buffer = ByteBuffer.allocate(capacity); // TODO think about allocateDirect()
    }

    public String read() {
        buffer.flip();
        byte[] destination = new byte[buffer.remaining()];
        buffer.get(destination);
        return new String(destination, StandardCharsets.UTF_8);
    }

    public void write(String s) {
        buffer.clear();
        buffer.put(s.getBytes());
    }

    public void write(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes);
    }

    public int readFromChannel(SocketChannel from) throws IOException {
        try {
            buffer.clear();
            return from.read(buffer);
        } catch (SocketException e) {
            return -1;
        }
    }

    public void writeIntoChannel(SocketChannel to) throws IOException {
        buffer.flip();
        while (buffer.hasRemaining()) {
            to.write(buffer);
        }
    }
}
