package bg.sofia.uni.fmi.mjt.dungeons.game;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchPlayerException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class PlayerManager {

    private static final List<Integer> allowedIds = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private PriorityQueue<Integer> freeIDs;
    private Map<Integer, SocketChannel> players;

    public PlayerManager() {
        this.players = new HashMap<>();
        this.freeIDs = new PriorityQueue<>(allowedIds);
    }

    public Map<Integer, SocketChannel> getAllPlayers() {
        return players;
    }

    public int getPlayerIdByChannel(SocketChannel channel) {
        return players.entrySet().stream()
                .filter(entry -> entry.getValue().equals(channel))
                .findFirst().orElseThrow(NoSuchPlayerException::new).getKey();
    }

    public int addNewPlayer(SocketChannel channel) throws PlayerCapacityReachedException {
        int playerId = getNextFreeId();
        players.put(playerId, channel);
        freeIDs.remove(playerId);
        System.out.printf("Adding new player with id %d\n", playerId);
        return playerId;
    }

    public int removePlayerByChannel(SocketChannel channel) {
        int playerId = getPlayerIdByChannel(channel);
        System.out.printf("Removing player with id %d\n", playerId);
        players.remove(playerId);
        freeIDs.add(playerId);
        return playerId;
    }

    private int getNextFreeId() throws PlayerCapacityReachedException {
        Integer nextFreeId = freeIDs.poll();
        if (nextFreeId == null) {
            throw new PlayerCapacityReachedException();
        }
        return nextFreeId;
    }
}
