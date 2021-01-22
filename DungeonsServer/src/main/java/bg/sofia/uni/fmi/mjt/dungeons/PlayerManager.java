package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchPlayerException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class PlayerManager {

    private static final List<Integer> allowedIds = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private PriorityQueue<Integer> freeIDs;
    private Set<Player> players;

    public PlayerManager() {
        this.players = new HashSet<>();
        this.freeIDs = new PriorityQueue<>(allowedIds);
    }

    public Player createNewPlayer(SocketChannel channel) throws PlayerCapacityReachedException {
        int playerId = getNextFreeId();
        Player player = new Player(playerId, channel);
        players.add(player);
        freeIDs.remove(playerId);
        System.out.printf("Adding new player with id %d\n", playerId);
        return player;
    }

    public Set<Player> getAllPlayers() {
        return players;
    }

    public Player getPlayerByChannel(SocketChannel channel) {
        return players.stream()
                .filter(player -> player.channel().equals(channel))
                .findFirst().orElseThrow(NoSuchPlayerException::new);
    }

    public void removePlayer(Player player) {
        System.out.printf("Removing player with id %d\n", player.id());
        players.remove(player);
        freeIDs.add(player.id());
    }

    private int getNextFreeId() throws PlayerCapacityReachedException {
        Integer nextFreeId = freeIDs.poll();
        if (nextFreeId == null) {
            throw new PlayerCapacityReachedException();
        }
        return nextFreeId;
    }
}
