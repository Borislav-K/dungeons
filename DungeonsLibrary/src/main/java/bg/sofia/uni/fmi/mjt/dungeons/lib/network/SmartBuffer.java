package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SmartBuffer {
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    private ByteBuffer buffer;

    public SmartBuffer() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public SmartBuffer(int capacity) {
        this.buffer = ByteBuffer.allocate(capacity); // TODO think about allocateDirect()
    }

    public ByteBuffer underlyingBuffer() {
        return buffer;//TODO encapsulate this
    }

    public void startSerialization() {
        buffer.clear();
    }

    public void startDeserialization() {
        buffer.flip();
    }

    public byte[] read() {
        buffer.flip();
        byte[] destination = new byte[buffer.remaining()];
        buffer.get(destination);
        return destination;
    }

    public void write(String s) {
        buffer.clear();
        buffer.put(s.getBytes());
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
        to.write(buffer);
    }
}
