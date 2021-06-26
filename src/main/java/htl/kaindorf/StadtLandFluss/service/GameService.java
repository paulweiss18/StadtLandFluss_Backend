package htl.kaindorf.StadtLandFluss.service;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.pojos.Round;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private LobbyStorage lobbyStorage = LobbyStorage.getInstance();
    private SocketHandler socketHandler;

    public GameService(SocketHandler socketHandler){
        this.socketHandler = socketHandler;
    }


    public void finishRound(String lobbyCode, String playerId, List<String> answers){

        int currentRound = lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().getCurrentRound();
        Player player = lobbyStorage.getPlayerObjById(playerId);
        boolean isFirstRequest = lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().getRounds().get(currentRound).getAnswers().isEmpty();
        lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().getRounds().get(currentRound).getAnswers().put(player, answers);

        if(isFirstRequest){
            socketHandler.finishRound(lobbyStorage.getLobbies().get(lobbyCode));
        }

        if(lobbyStorage.getLobbies().get(lobbyCode).getGamePlay().getRounds().get(currentRound).getAnswers().keySet().size() == lobbyStorage.getLobbies().get(lobbyCode).getPlayers().size()){
            System.out.println("all submitted their answers");
        }

    }




}
