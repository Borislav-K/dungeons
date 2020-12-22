package bg.sofia.uni.fmi.mjt.dungeons.game;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchPlayerException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;

import java.nio.channels.SocketChannel;
import java.util.*;

public class PlayerManager {

    private static final List<Integer> allowedIds = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    Map<Integer, SocketChannel> players;

    private PriorityQueue<Integer> freeIDs;

    public PlayerManager() {
        this.players = new HashMap<>();
        this.freeIDs = new PriorityQueue<>(allowedIds);
    }

    public List<SocketChannel> getAllPlayers() {
        // The modification of players is done only through the other methods of this class
        return Collections.unmodifiableList(new LinkedList<>(players.values()));
    }

    public SocketChannel getChannelFor(int playerId) throws NoSuchPlayerException {
        SocketChannel playerChannel = players.get(playerId);
        if (playerChannel == null) {
            throw new NoSuchPlayerException();
        }
        return playerChannel;
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

    public void removePlayerById(int playerId) throws NoSuchPlayerException {
        System.out.printf("Removing player with id %d\n", playerId);
        if (players.containsKey(playerId)) {
            players.remove(playerId);
            freeIDs.add(playerId);
        } else {
            throw new NoSuchPlayerException();
        }
    }

    public void removePlayerByChannel(SocketChannel channel) {
        int playerId = getPlayerIdByChannel(channel);
        System.out.printf("Removing player with id %d\n", playerId);
        players.remove(playerId);
        freeIDs.add(playerId);
    }


    private int getNextFreeId() throws PlayerCapacityReachedException {
        Integer nextFreeId = freeIDs.poll();
        if (nextFreeId == null) {
            throw new PlayerCapacityReachedException();
        }
        return nextFreeId;
    }
}
