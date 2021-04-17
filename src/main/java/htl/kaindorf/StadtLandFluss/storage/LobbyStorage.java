package htl.kaindorf.StadtLandFluss.storage;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyStorage {

    private static Map<String, Lobby> lobbies;
    private static LobbyStorage instance;

    private LobbyStorage(){
        lobbies = new HashMap<>();
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

}
