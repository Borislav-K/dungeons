package bg.sofia.uni.fmi.mjt.dungeons.client.network;

import bg.sofia.uni.fmi.mjt.dungeons.client.SmartBuffer;
import bg.sofia.uni.fmi.mjt.dungeons.client.rendering.RenderableMap;

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
        socketChannel = SocketChannel.open();
        socketChannel.connect(SERVER_ADDRESS);
        System.out.println("Connected to the server.");
    }

    public void disconnect() {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            System.out.println("Could not properly terminate the connection to the server");
            e.printStackTrace();
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

    public char[][] fetchMapFromServer() {
        try {
            int r = buffer.readFromChannel(socketChannel);
            if (r <= 0) {
                return null; //TODO handle this in a better way
            }
        } catch (IOException e) {
            throw new IllegalStateException("Server stopped responding", e);//TODO handle this in a better way
        }
        return deserializeMap(buffer);
    }


    private char[][] deserializeMap(SmartBuffer buffer) {
        int mapSize = RenderableMap.MAP_DIMENSIONS;

        byte[] mapBytes = buffer.read(mapSize * mapSize);
        char[][] map = new char[mapSize][mapSize];
        for (int i = 0; i < mapBytes.length; i++) {
            map[i / mapSize][i % mapSize] = (char) mapBytes[i];
        }
        return map;
    }

}
