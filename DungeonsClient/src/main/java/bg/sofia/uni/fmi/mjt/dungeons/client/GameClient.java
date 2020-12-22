package bg.sofia.uni.fmi.mjt.dungeons.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class GameClient {


    private InetSocketAddress serverAddress;
    private SmartBuffer buffer;
    private SocketChannel socketChannel;

    public GameClient(String serverHost, int serverPort) {
        this.serverAddress = new InetSocketAddress(serverHost, serverPort);
        this.buffer = new SmartBuffer();
    }

    public void start() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(serverAddress);
        System.out.println("Connected to the server.");

        ServerInputHandler serverInputHandler = new ServerInputHandler(socketChannel);
        serverInputHandler.start();
    }

    public void stop() {
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

}
