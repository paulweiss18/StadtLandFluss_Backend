package htl.kaindorf.StadtLandFluss.service;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.LobbyStatus;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LobbyService {


    //if a player creates a new Lobby
    public Lobby createLobby(Player player){
        Lobby lobby = new Lobby();
        lobby.setLobbyCode(UUID.randomUUID().toString());
        lobby.setLobbyLeaderPlayer(player);
        lobby.setStatus(LobbyStatus.CREATED);

        //store Lobby in Storage
        LobbyStorage.getInstance().setLobby(lobby);
        return lobby;
    }

    //if a player wants to join a already existing Lobby via the LobbyCode
    public Lobby joinLobby(Player player, String lobbyCode){
        if(LobbyStorage.getInstance().getLobbies().get(lobbyCode).getStatus().equals(LobbyStatus.CREATED)){
            LobbyStorage.getInstance().getLobbies().get(lobbyCode).getPlayers().add(player);
            return LobbyStorage.getInstance().getLobbies().get(lobbyCode);
        }
        return null;
    }

}
