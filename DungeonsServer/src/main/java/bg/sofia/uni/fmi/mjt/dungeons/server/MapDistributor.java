package bg.sofia.uni.fmi.mjt.dungeons.server;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

public class MapDistributor {

    private Set<SocketChannel> users;


    public MapDistributor() {
        this.users = new HashSet<>();
    }

}
