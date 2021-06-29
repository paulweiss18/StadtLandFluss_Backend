package htl.kaindorf.StadtLandFluss.service;

import htl.kaindorf.StadtLandFluss.exception.LobbyJoinException;
import htl.kaindorf.StadtLandFluss.logic.GamePlay;
import htl.kaindorf.StadtLandFluss.pojos.*;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service Class for Lobby, connection between Controller and Storage
 */
@Service
public class LobbyService {

    private List<String> defaultCategories = Arrays.asList((new String[]{"City","Country","River","Mountain"}));
    private SocketHandler socketHandler = new SocketHandler();

    /**
     * createPlayer creates a new Player in the Storage
     * @param name
     * @return created Player Object
     */
    public Player createPlayer(String name){
        Player player = new Player(UUID.randomUUID().toString(), name,0, null);
        LobbyStorage.getInstance().getPlayers().add(player);
        return player;
    }


    /**
     * getLobbyByCode returns the Lobby by LobbyCode
     * @param lobbyCode
     * @return Lobby
     */
    public Lobby getLobbyByCode(String lobbyCode){
        return LobbyStorage.getInstance().getLobbies().get(lobbyCode);
    }


    /**
     * createLobby creates Lobby in Lobby Storage
     * @param player player who creates Lobby
     * @return Lobby
     */
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

    /**
     * joinLobby if a player wants to join a Lobby
     * @param lobbyCode lobbyCode of Lobby
     * @param player already created Player wants to join
     * @return Lobby when successfull joined
     * @Exception is thrown when Game already Started or Code is invalid
     */
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

            //updates Players in Lobby, that Player has joined
            socketHandler.updateLobby(lobbyCode);

            return LobbyStorage.getInstance().getLobbies().get(lobbyCode);
        }
    }

    /**
     * configureGameSettings sets new Configuration in Storage
     * @param userId player who wants to edit Configuration
     * @param lobbyCode lobby which is edited
     * @param numberOfRounds
     * @param categories List of categories
     * @param excludedLetters List of letters which should be excluded in the Game
     * @return Lobby if successfully edited
     */
    public Lobby configureGameSettings(String userId, String lobbyCode, int numberOfRounds, List<String> categories, List<String> excludedLetters){

        Lobby lobby = LobbyStorage.getInstance().getLobbies().get(lobbyCode);

        if(lobby.getLobbyLeaderPlayer().getUserid().equals(userId) && lobby.getStatus().equals(LobbyStatus.CREATED)){

            lobby.getGameConfiguration().setNumberOfRounds(numberOfRounds);
            lobby.getGameConfiguration().setCategories(categories);
            lobby.getGameConfiguration().setExcludedLetters(excludedLetters);

            socketHandler.updateLobby(lobbyCode);

            return lobby;
        }else{
            return null;
        }
    }


    /**
     * startGame method to start a new Game - change State of Lobby to IN_GAME
     * @param userId who wants to start the Game
     * @param lobbyCode which is started
     * @return boolean whether start was successful
     */
    public boolean startGame(String userId, String lobbyCode){
        Lobby lobby = LobbyStorage.getInstance().getLobbies().get(lobbyCode);

        if(lobby.getLobbyLeaderPlayer().getUserid().equals(userId) && lobby.getStatus().equals(LobbyStatus.CREATED) && lobby.getPlayers().size()>1){

            lobby.setGamePlay(new GamePlay(lobby, this.socketHandler));
            lobby.setStatus(LobbyStatus.IN_GAME);

            return true;
        }else{
            //Cannot start Game
            return false;
        }
    }





}
