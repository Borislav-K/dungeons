package bg.sofia.uni.fmi.mjt.dungeons.network;


import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;
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

    private SmartBuffer buffer;
    private SocketChannel socketChannel;

    public GameClient() {
        this.buffer = new SmartBuffer();
    }

    public void connect() throws IOException {
        socketChannel = SocketChannel.open(SERVER_ADDRESS);
        socketChannel.configureBlocking(false);
        System.out.println("Connected to the server.");
    }

    public void disconnect() {
        try {
            socketChannel.close();
            System.out.println("Disconnected from the server");
        } catch (IOException e) {
            System.out.println("Could not gracefully terminate connection to the server.");
        }
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
            return r > 0 ? deserializePlayerSegment(buffer) : null;
        } catch (IOException e) {
            return null;
        }
    }

    private PlayerSegment deserializePlayerSegment(SmartBuffer buffer) {
        byte[] receivedBytes = buffer.read();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receivedBytes);
             DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream)) {
            PlayerSegment playerSegment = new PlayerSegment();
            playerSegment.deserialize(dataInputStream);
            return playerSegment;
        } catch (IOException e) {
            throw new RuntimeException("Could not deserialize game state", e);
        }
    }

}
