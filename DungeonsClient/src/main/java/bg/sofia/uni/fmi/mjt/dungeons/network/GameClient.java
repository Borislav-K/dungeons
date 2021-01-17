package bg.sofia.uni.fmi.mjt.dungeons.network;


import bg.sofia.uni.fmi.mjt.dungeons.lib.network.SmartBuffer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class GameClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 10_000;
    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(SERVER_HOST, SERVER_PORT);

    private static final int SEGMENT_LENGTH = 1024;

    private SmartBuffer buffer;
    private SocketChannel socketChannel;

    public GameClient() {
        this.buffer = new SmartBuffer(SEGMENT_LENGTH);
    }

    public void connect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(SERVER_ADDRESS);
        System.out.println("Connected to the server.");
    }

    public void sendMessage(String msg) {
        buffer.write(msg);
        try {
            buffer.writeIntoChannel(socketChannel);
        } catch (IOException e) {
            System.out.printf("Could not send %s to the server%n", msg);
            e.printStackTrace();
        }
    }

    public PlayerSegment fetchStateFromServer() {
        try {
            int r = buffer.readFromChannel(socketChannel);
            if (r <= 0) {
                return null; //TODO handle this in a better way
            }
            return deserializeState(buffer);
        } catch (IOException e) {
            throw new IllegalStateException("Server stopped responding", e);//TODO handle this in a better way
        }
    }


    private PlayerSegment deserializeState(SmartBuffer buffer) {
        byte[] mapBytes = buffer.read();
        System.out.println("LENGTH: " + mapBytes.length); //TODO remove
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mapBytes);
             DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream)) {
            PlayerSegment playerSegment = new PlayerSegment();
            playerSegment.deserialize(dataInputStream);
            return playerSegment;
        } catch (IOException e) {
            throw new RuntimeException("Could not deserialize game state", e);
        }
    }

}
