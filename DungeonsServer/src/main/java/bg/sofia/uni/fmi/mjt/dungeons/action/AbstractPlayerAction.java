package bg.sofia.uni.fmi.mjt.dungeons.action;

import java.nio.channels.SocketChannel;

public abstract class AbstractPlayerAction implements PlayerAction {

    protected SocketChannel initiator;

    protected AbstractPlayerAction(SocketChannel initiator) {
        this.initiator = initiator;
    }

    @Override
    public SocketChannel initiator() {
        return initiator;
    }
}
