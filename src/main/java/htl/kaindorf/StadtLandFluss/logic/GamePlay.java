package htl.kaindorf.StadtLandFluss.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import htl.kaindorf.StadtLandFluss.pojos.GameConfiguration;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Round;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import lombok.Data;

import java.util.*;


@Data
public class GamePlay {
    private List<Round> rounds;
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


    public void newRound(){
        currentRound++;

        int index = random.nextInt(letters.size()-1);
        letters.remove(index);

        Round round = new Round(letters.get(index), null);
        currentLetter = letters.get(index);
        System.out.println(currentLetter);

        //WebSocket start new Round
        socketHandler.startGame(lobby.getLobbyCode(), this);
    }





}
