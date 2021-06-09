package htl.kaindorf.StadtLandFluss.logic;

import htl.kaindorf.StadtLandFluss.pojos.GameConfiguration;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Round;
import lombok.Data;

import java.util.*;


@Data
public class GamePlay {
    private List<Round> rounds;
    private int currentRounds;
    private Lobby lobby;
    List<String> letters = new ArrayList<>();
    Random random = new Random();


    public GamePlay(Lobby lobby){
        this.lobby = lobby;
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
        currentRounds++;

        int index = random.nextInt(letters.size());
        letters.remove(index);

        Round round = new Round(letters.get(index), null);



        //WebSocket start new Round



    }




}
