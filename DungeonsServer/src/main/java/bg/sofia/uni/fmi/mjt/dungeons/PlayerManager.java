package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchPlayerException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
// PlayerManager maintains a mapping between clients(channels) and their respective player entities
public class PlayerManager {

    private static final List<Integer> allowedIds = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private PriorityQueue<Integer> freeIDs;
    private Map<Player, SocketChannel> players;

    public PlayerManager() {
        this.players = new HashMap<>();
        this.freeIDs = new PriorityQueue<>(allowedIds);
    }

    public Player createNewPlayer(SocketChannel channel) throws PlayerCapacityReachedException {
        int playerId = getNextFreeId();
        Player player = new Player(playerId);
        players.put(player, channel);
        freeIDs.remove(playerId);
        System.out.printf("Adding new player with id %d\n", playerId);
        return player;
    }

    public Set<Player> getAllPlayers() {
        return players.keySet();
    }

    public Player getPlayerByChannel(SocketChannel channel) throws NoSuchPlayerException {
        return players.entrySet().stream()
                .filter(playerEntry -> playerEntry.getValue().equals(channel))
                .findFirst().orElseThrow(NoSuchPlayerException::new)
                .getKey();
    }

    public SocketChannel getPlayerChannel(Player player) {
        return players.get(player);
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
