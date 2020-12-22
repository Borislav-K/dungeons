package bg.sofia.uni.fmi.mjt.dungeons.client;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ServerInputHandler extends Thread {

    private static final int SQUARE_SIZE = 10;

    private SocketChannel socketChannel;
    private SmartBuffer buffer;

    public ServerInputHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.buffer = new SmartBuffer();
        this.setDaemon(true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                int r = buffer.readFromChannel(socketChannel);
                if (r <= 0) {
                    return;//TODO handle this in a better way if possible
                }
                char[][] map = deserializeMap(buffer);
                renderMap(map);
            }
        } catch (IOException e) {
            System.out.println("Server-listening thread is dead");
            e.printStackTrace();
        }
    }

    public void renderMap(char[][] map) { //TODO remove
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println("\n");
        }
    }


    private char[][] deserializeMap(SmartBuffer buffer) {
        byte[] mapBytes = buffer.read().getBytes();
        char[][] map = new char[SQUARE_SIZE][SQUARE_SIZE];
        for (int i = 0; i < mapBytes.length; i++) {
            map[i / SQUARE_SIZE][i % SQUARE_SIZE] = (char) mapBytes[i];
        }
        return map;
    }
}
