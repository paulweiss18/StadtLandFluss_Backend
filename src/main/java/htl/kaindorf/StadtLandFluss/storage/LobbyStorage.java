package htl.kaindorf.StadtLandFluss.storage;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Player getPlayerObjById(String id){
        for(Player player : players){
            if(player.getUserid().equals(id)){
                return player;
            }
        }
        return null;
    }

}
