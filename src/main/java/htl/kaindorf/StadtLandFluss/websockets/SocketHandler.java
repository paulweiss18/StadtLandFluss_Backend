package htl.kaindorf.StadtLandFluss.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import htl.kaindorf.StadtLandFluss.logic.GamePlay;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.pojos.Round;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import htl.kaindorf.StadtLandFluss.websockets.message.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class SocketHandler extends TextWebSocketHandler {

    private LobbyStorage lobbyStorage = LobbyStorage.getInstance();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        Map<String, String> valueMap = new ObjectMapper().readValue(message.getPayload(), LinkedHashMap.class);

        if(valueMap.get("type").equals("init")){
            String playerId = valueMap.get("playerId");
            if(lobbyStorage.getPlayerObjById(playerId).getSession() != null){
                lobbyStorage.getPlayerObjById(playerId).getSession().close();
                lobbyStorage.getPlayerObjById(playerId).setSession(null);
            }
            lobbyStorage.getPlayerObjById(playerId).setSession(session);

        }else if(valueMap.get("type").equals("connection_closed")){

            String playerId = valueMap.get("playerId");

            Lobby lobby = lobbyStorage.getLobbyFromPlayer(playerId);
            lobbyStorage.removePlayerFromLobby(playerId);

            if(lobby.getLobbyCode() != null){
                updateLobby(lobby.getLobbyCode());
            }

            session.close();
        }

    }

    public void updateLobby(String lobbyCode){
        for(Player player : lobbyStorage.getLobbies().get(lobbyCode).getPlayers()){
            try {
                if(player.getSession() != null){
                    player.getSession().sendMessage(new TextMessage(mapper.writeValueAsString(new Message("updateLobby", lobbyStorage.getLobbies().get(lobbyCode)))));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    public void startGame(String lobbyCode, GamePlay gamePlay){
        for(Player player : lobbyStorage.getLobbies().get(lobbyCode).getPlayers()){
            try {
                player.getSession().sendMessage(new TextMessage(mapper.writeValueAsString(new Message("startGame", gamePlay))));
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }



}
