package htl.kaindorf.StadtLandFluss.logic;

import htl.kaindorf.StadtLandFluss.pojos.GameConfiguration;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Round;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import lombok.Data;

import java.util.*;


@Data
public class GamePlay {
    private List<Round> rounds;
    private int currentRounds;
    private Lobby lobby;
    List<String> letters = new ArrayList<>();
    Random random = new Random();
    SocketHandler socketHandler;



    public GamePlay(Lobby lobby, SocketHandler socketHandler){
        this.lobby = lobby;
        this.socketHandler = socketHandler;

        currentRounds = -1;

        for(int i = 65; i <= 90; i++){
            char c = (char) i;
            if(!lobby.getGameConfiguration().getExcludedLetters().contains(""+c)){
                letters.add(c+"");
            }
        }
        newRound();
    }


    public void newRound(){
        System.out.println("new round");
        currentRounds++;

        int index = random.nextInt(letters.size());
        letters.remove(index);

        Round round = new Round(letters.get(index), null);

        //WebSocket start new Round
        socketHandler.startGame(lobby.getLobbyCode(), round);
    }





}
