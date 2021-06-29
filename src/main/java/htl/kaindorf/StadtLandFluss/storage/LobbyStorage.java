package htl.kaindorf.StadtLandFluss.storage;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lobby Storage Class which stores all Lobbies and Players - Singleton
 */
public class LobbyStorage {

    private static Map<String, Lobby> lobbies;
    private static LobbyStorage instance;
    private static List<Player> players;

    private LobbyStorage(){
        lobbies = new HashMap<>();
        players = new ArrayList<>();
    }

    public static synchronized LobbyStorage getInstance(){
        if(instance == null){
            instance = new LobbyStorage();
        }
        return instance;
    }

    public void setLobby(Lobby lobby){
        lobbies.put(lobby.getLobbyCode(), lobby);
    }

    public Map<String, Lobby> getLobbies(){
        return lobbies;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * method to get the Player Object with the id
     * @param id of Player
     * @return Player Object with the Player Id
     */
    public Player getPlayerObjById(String id){
        for(Player player : players){
            if(player.getUserid().equals(id)){
                return player;
            }
        }
        return null;
    }

    /**
     * removes player of Lobby
     * @param playerId of Player
     */
    public void removePlayerFromLobby(String playerId){
        Lobby lobby = null;

        for(String lobbyCode : lobbies.keySet()){
            for(Player player : lobbies.get(lobbyCode).getPlayers()){
                if(player.getUserid().equals(playerId)){
                    lobby = lobbies.get(lobbyCode);
                }
            }
        }

        if(lobby != null){
            lobby.getPlayers().removeIf(p -> p.getUserid().equals(playerId));
        }
    }

    /**
     * returns Lobby of a single Player
     * @param playerId of Player
     * @return Lobby Object
     */
    public Lobby getLobbyFromPlayer(String playerId){
        for(String lobbyCode : lobbies.keySet()){
            for(Player player : lobbies.get(lobbyCode).getPlayers()){
                if(player.getUserid().equals(playerId)){
                    return lobbies.get(lobbyCode);
                }
            }
        }
        return null;
    }


}
