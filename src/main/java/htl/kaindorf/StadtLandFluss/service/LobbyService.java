package htl.kaindorf.StadtLandFluss.service;

import htl.kaindorf.StadtLandFluss.exception.LobbyJoinException;
import htl.kaindorf.StadtLandFluss.logic.GamePlay;
import htl.kaindorf.StadtLandFluss.pojos.*;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LobbyService {

    private List<String> defaultCategories = Arrays.asList((new String[]{"City","Country","River","Mountain"}));
    private SocketHandler socketHandler = new SocketHandler();

    //If a new Player is created
    public Player createPlayer(String name){
        Player player = new Player(UUID.randomUUID().toString(), name, null);
        LobbyStorage.getInstance().getPlayers().add(player);
        return player;
    }

    public Lobby getLobbyByCode(String lobbyCode){
        return LobbyStorage.getInstance().getLobbies().get(lobbyCode);
    }


    //if a player creates a new Lobby
    public Lobby createLobby(Player player){
        List<Player> players = new ArrayList<>();
        players.add(player);

        //Set Gameplay and default settings
        GameConfiguration defaultConfiguration = new GameConfiguration(5, defaultCategories, new ArrayList<>());

        Lobby lobby = new Lobby(UUID.randomUUID().toString(), players, LobbyStatus.CREATED, player, defaultConfiguration, null);

        //store Lobby in Storage
        LobbyStorage.getInstance().setLobby(lobby);
        return lobby;
    }


    //if a player wants to join a already existing Lobby via the LobbyCode
    // + Excpetion Handling
    public Lobby joinLobby(Player player, String lobbyCode) throws LobbyJoinException {
        if(LobbyStorage.getInstance().getLobbies().get(lobbyCode) == null) {
            throw new LobbyJoinException("No lobby with this code");
        }
        else if(!(LobbyStorage.getInstance().getLobbies().get(lobbyCode).getStatus().equals(LobbyStatus.CREATED))){
            throw new LobbyJoinException("Game with this GameID has already started or ended");
        }
        else{

            Lobby currentLobby = LobbyStorage.getInstance().getLobbies().get(lobbyCode);
            currentLobby.getPlayers().add(player);

            socketHandler.updateLobbyAfterUserJoined(currentLobby.getPlayers());

            return LobbyStorage.getInstance().getLobbies().get(lobbyCode);
        }
    }

    public Lobby configureGameSettings(String userId, String lobbyCode, int numberOfRounds, List<String> categories, List<String> excludedLetters){

        Lobby lobby = LobbyStorage.getInstance().getLobbies().get(lobbyCode);
        System.out.println(lobby);
        System.out.println(userId);

        if(lobby.getLobbyLeaderPlayer().getUserid().equals(userId) && lobby.getStatus().equals(LobbyStatus.CREATED)){

            lobby.getGameConfiguration().setNumberOfRounds(numberOfRounds);
            lobby.getGameConfiguration().setCategories(categories);
            lobby.getGameConfiguration().setExcludedLetters(excludedLetters);

            return lobby;
        }else{
            return null;
        }
    }



    public boolean startGame(String userId, String lobbyCode){
        Lobby lobby = LobbyStorage.getInstance().getLobbies().get(lobbyCode);

        if(lobby.getLobbyLeaderPlayer().getUserid().equals(userId) && lobby.getStatus().equals(LobbyStatus.CREATED) && lobby.getPlayers().size()>1){

            lobby.setGamePlay(new GamePlay(lobby));
            lobby.setStatus(LobbyStatus.IN_GAME);

            return true;
        }else{

            //Cannot start Game
            return false;
        }
    }





}
