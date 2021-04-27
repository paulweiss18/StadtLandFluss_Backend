package htl.kaindorf.StadtLandFluss.service;

import htl.kaindorf.StadtLandFluss.exceptions.GameAlreadyStartedExcpetion;
import htl.kaindorf.StadtLandFluss.exceptions.LobbyNotFoundException;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.LobbyStatus;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LobbyService {


    //if a player creates a new Lobby
    public Lobby createLobby(Player player){
        List<Player> players = new ArrayList<>();
        players.add(player);
        Lobby lobby = new Lobby(UUID.randomUUID().toString(), players, LobbyStatus.CREATED, player);

        //store Lobby in Storage
        LobbyStorage.getInstance().setLobby(lobby);
        return lobby;
    }


    //if a player wants to join a already existing Lobby via the LobbyCode
    // + Excpetion Handling
    public Lobby joinLobby(Player player, String lobbyCode) throws LobbyNotFoundException, GameAlreadyStartedExcpetion{
        if(LobbyStorage.getInstance().getLobbies().get(lobbyCode) == null) {
            throw new LobbyNotFoundException("No lobby with this code");
        }
        else if(!(LobbyStorage.getInstance().getLobbies().get(lobbyCode).getStatus().equals(LobbyStatus.CREATED))){
            throw new GameAlreadyStartedExcpetion("Game with this GameID has already started");
        }
        else{
            LobbyStorage.getInstance().getLobbies().get(lobbyCode).getPlayers().add(player);
            return LobbyStorage.getInstance().getLobbies().get(lobbyCode);
        }
    }

}
