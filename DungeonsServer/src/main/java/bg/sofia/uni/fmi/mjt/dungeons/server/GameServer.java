package bg.sofia.uni.fmi.mjt.dungeons.server;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;
import bg.sofia.uni.fmi.mjt.dungeons.game.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.game.event.EventBus;
import bg.sofia.uni.fmi.mjt.dungeons.game.event.EventFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class GameServer extends Thread {

    private static final String HOST = "localhost";
    private static final int PORT = 10_000;
    private SmartBuffer buffer;
    private boolean stopFlag;
    private EventBus eventBus;
    private PlayerManager playerManager;

    public GameServer(EventBus eventBus) {
        this.eventBus = eventBus;
        this.buffer = new SmartBuffer();
        this.stopFlag = false;
        this.playerManager = new PlayerManager();
    }

    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (!stopFlag) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        int r = buffer.readFromChannel(sc);
                        if (r <= 0) {
                            System.out.println("A player disconnected manually");
                            sc.close();
                            playerManager.removePlayerByChannel(sc);
                            break;
                        }
                        try {
                            String receivedData = buffer.read();
                            eventBus.publish(EventFactory.create(receivedData, playerManager.getPlayerIdByChannel(sc)));
                            System.out.printf("Received message: %s\n", receivedData);
                            for (SocketChannel ch : playerManager.getAllPlayers()) {
                                buffer.write(receivedData);
                                try {
                                    buffer.writeIntoChannel(ch);
                                } catch (SocketException e) {
                                    System.out.println("A user has manually disconnected");
                                    ch.close();
                                    playerManager.removePlayerByChannel(ch);
                                }
                            }
                        } catch (SocketException e) {
                            System.out.println("A user has manually disconnected");
                            sc.close();
                        }


                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel playerChannel = sockChannel.accept();
                        playerChannel.configureBlocking(false);
                        try {
                            playerManager.addNewPlayer(playerChannel);
                            playerChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("A player has connected");
                        } catch (PlayerCapacityReachedException e) {
                            String sorryMessage = "Sorry. Game is full. Try again later";
                            buffer.write(sorryMessage);
                            buffer.writeIntoChannel(playerChannel);
                            System.out.println("Game is full - closing channel");
                            playerChannel.close();
                        }
                    }

                    keyIterator.remove();
                }

            }

        } catch (IOException e) {
            System.out.println("There is a problem with the server socket");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.stopFlag = true;
    }

}
