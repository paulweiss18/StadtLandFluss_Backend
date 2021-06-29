package htl.kaindorf.StadtLandFluss.service;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.pojos.Round;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import org.springframework.stereotype.Service;

import java.text.spi.CollatorProvider;
import java.util.Comparator;
import java.util.List;


/**
 * Service Class for Game connection between Controller and Storage
 */
@Service
public class GameService {

    private LobbyStorage lobbyStorage = LobbyStorage.getInstance();
    private SocketHandler socketHandler;

    /**
     * Constructor
     * @param socketHandler is passed to GameService
     */
    public GameService(SocketHandler socketHandler){
        this.socketHandler = socketHandler;
    }


    /**
     * finishRound sets Answers for the different Categories, access to Storage - checks if first submit or all have already submitted
     * @param lobbyCode
     * @param playerId
     * @param answers as a List of answers
     */
    public void finishRound(String lobbyCode, String playerId, List<String> answers){

        int currentRound = lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().getCurrentRound();
        Player player = lobbyStorage.getPlayerObjById(playerId);
        boolean isFirstRequest = lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().getRounds().get(currentRound).getAnswers().isEmpty();
        lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().getRounds().get(currentRound).getAnswers().put(player.getUserid(), answers);

        if(isFirstRequest){
            socketHandler.finishRound(lobbyStorage.getLobbies().get(lobbyCode), player);
        }

        if(lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().getRounds().get(currentRound).getAnswers().keySet().size() == lobbyStorage.getLobbies().get(lobbyCode).getPlayers().size()){
            socketHandler.lastPlayerFinished(lobbyStorage.getLobbies().get(lobbyCode));
        }

    }

    /**
     * votePlayer sets Score in Storage
     * @param playerId
     * @param score Number of Points for a specific Player
     */
    public void votePlayer(String playerId, int score){
        lobbyStorage.getPlayerObjById(playerId).setScore(score);
        //lobbyStorage.getLobbyFromPlayer(playerId).getPlayers().sort(Comparator.comparing(Player::getScore).reversed().thenComparing(Player::getUsername));
    }

    /**
     * nextRound
     * @param lobbyCode
     */
    public void nextRound(String lobbyCode){
        lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().newRound();
    }

}
