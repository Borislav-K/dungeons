package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.IllegalPlayerActionException;
import bg.sofia.uni.fmi.mjt.dungeons.action.*;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.SmartBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class GameServer {

    private static final String HOST = "localhost";
    private static final int PORT = 10_000;

    // All player actions are encoded by a string of 3 characters
    private static final int ACTION_ENCODING_LENGTH = 3;

    private SmartBuffer buffer;
    private PlayerActionHandler actionHandler;

    private Selector selector;

    public GameServer(PlayerActionHandler actionHandler) {
        this.actionHandler = actionHandler;
        this.buffer = new SmartBuffer();
    }

    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot start the server", e);
        }
    }

    public void fetchPlayerActions() {
        try {
            int readyChannels = selector.selectNow();
            if (readyChannels == 0) {
                return;
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
                        actionHandler.publish(new PlayerDisconnect(sc));
                        break;
                    }
                    String msg = new String(buffer.read(), StandardCharsets.UTF_8);
                    publishPlayerActions(msg, sc);
                } else if (key.isAcceptable()) {
                    registerNewPlayer(key);
                }
                keyIterator.remove();
            }
        } catch (IOException e) {
            throw new IllegalStateException("There was a problem with network communication", e);
        }
    }

    private void publishPlayerActions(String playerInput, SocketChannel sc) {
        if (playerInput.length() % ACTION_ENCODING_LENGTH != 0) {
            System.out.printf("Received an input that does not comply with the protocol: %s\n", playerInput);
            return;
        }
        for (int i = 0; i < playerInput.length(); i += ACTION_ENCODING_LENGTH) {
            String nextActionString = playerInput.substring(i, i + ACTION_ENCODING_LENGTH);
            System.out.printf("Received player action: %s\n", nextActionString);
            try {
                actionHandler.publish(PlayerAction.of(nextActionString, sc));
            } catch (IllegalPlayerActionException e) {

            }
        }
    }

    private void registerNewPlayer(SelectionKey acceptableKey) {
        try {
            ServerSocketChannel sockChannel = (ServerSocketChannel) acceptableKey.channel();
            SocketChannel playerChannel = sockChannel.accept();
            playerChannel.configureBlocking(false);
            actionHandler.publish(new PlayerConnect(playerChannel));
            playerChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("Could not register a new player");
            e.printStackTrace();
        }
    }

}
