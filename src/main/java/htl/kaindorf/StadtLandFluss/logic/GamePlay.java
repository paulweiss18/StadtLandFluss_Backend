package htl.kaindorf.StadtLandFluss.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import htl.kaindorf.StadtLandFluss.pojos.*;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import lombok.Data;

import java.util.*;

/**
 * GamePlay Class - is responsible for saving the GamePlay Data (answers, currentRound)
 */
@Data
public class GamePlay {
    private List<Round> rounds = new ArrayList<>();
    private int currentRound;
    private String currentLetter;

    @JsonIgnore
    private Lobby lobby;
    @JsonIgnore
    List<String> letters = new ArrayList<>();
    @JsonIgnore
    Random random = new Random();
    @JsonIgnore
    SocketHandler socketHandler;


    /**
     * constructor: valid letters are generated and first round will start
     * @param lobby to create a Gameplay Object for a specific Lobby
     * @param socketHandler is passed in the constructor
     */
    public GamePlay(Lobby lobby, SocketHandler socketHandler){
        this.lobby = lobby;
        this.socketHandler = socketHandler;

        currentRound = -1;

        for(int i = 65; i <= 90; i++){
            char c = (char) i;
            if(!lobby.getGameConfiguration().getExcludedLetters().contains(""+c)){
                letters.add(c+"");
            }
        }
        newRound();
    }

    /**
     * newRound() generates a Random Letter and informs Players
     * calls Method startGame(WebSocket) which informs Players with the current Round
     */
    public void newRound(){
        currentRound++;

        if(currentRound < lobby.getGameConfiguration().getNumberOfRounds()) {

            //Random Letter is generated
            int index = random.nextInt(letters.size() - 1);
            letters.remove(index);

            Round round = new Round(letters.get(index), new HashMap<String, List<String>>());
            currentLetter = letters.get(index);
            rounds.add(round);

            //WebSocket start new Round
            if(currentRound == 0){
                socketHandler.startGame(lobby.getLobbyCode(), this);
            }else{
                socketHandler.nextRound(lobby);
            }

        }else{

            //finish Game
            lobby.setStatus(LobbyStatus.FINISHED);
            socketHandler.finishGame(lobby);
        }
    }





}
