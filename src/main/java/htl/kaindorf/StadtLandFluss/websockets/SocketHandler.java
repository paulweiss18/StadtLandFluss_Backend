package htl.kaindorf.StadtLandFluss.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import htl.kaindorf.StadtLandFluss.logic.GamePlay;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.pojos.Round;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import htl.kaindorf.StadtLandFluss.websockets.message.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SocketHandler manages all WebSocket connections
 */
@Component
public class SocketHandler extends TextWebSocketHandler {

    private LobbyStorage lobbyStorage = LobbyStorage.getInstance();
    ObjectMapper mapper = new ObjectMapper();


    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status){
        System.out.println("websocket connection closed: "+ session.getId());
    }

    /**
     * Handles incoming Messages
     * @param session
     * @param message
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        try {
            System.out.println(message.getPayload());
            Map<String, String> valueMap = new ObjectMapper().readValue(message.getPayload(), LinkedHashMap.class);

            //when a player has established a connection - to link Session to Player
            if (valueMap.get("type").equals("init")) {
                String playerId = valueMap.get("playerId");
                if (lobbyStorage.getPlayerObjById(playerId).getSession() != null) {
                    lobbyStorage.getPlayerObjById(playerId).getSession().close();
                    lobbyStorage.getPlayerObjById(playerId).setSession(null);
                }
                lobbyStorage.getPlayerObjById(playerId).setSession(session);

                //when a player wants to disconnect
            } else if (valueMap.get("type").equals("connection_closed")) {
                String playerId = valueMap.get("playerId");

                Lobby lobby = lobbyStorage.getLobbyFromPlayer(playerId);
                lobbyStorage.removePlayerFromLobby(playerId);

                if (lobby.getLobbyCode() != null) {
                    updateLobby(lobby.getLobbyCode());
                }
                session.close();
            }

        }catch (Exception ex){
            System.out.println("websocket - error");
        }

    }

    /**
     * Method which Sends Update Information to all Players of a Lobby
     * @param lobbyCode
     */
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

    /**
     * Method to inform all Players with the new Round
     * @param lobbyCode
     * @param round
     */
    public void startGame(String lobbyCode, GamePlay round){
        for(Player player : lobbyStorage.getLobbies().get(lobbyCode).getPlayers()){
            try {
                player.getSession().sendMessage(new TextMessage(mapper.writeValueAsString(new Message("startGame", round))));
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * informs other players that someone has finished
     * @param lobby
     * @param finished_player
     */
    public void finishRound(Lobby lobby, Player finished_player){
        for(Player player : lobby.getPlayers()){
            try {
                if(!player.equals(finished_player)){
                    player.getSession().sendMessage(new TextMessage(mapper.writeValueAsString(new Message("finishRound", ""))));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * informs Players that all Players have submitted their answers
     * @param lobby
     */
    public void lastPlayerFinished(Lobby lobby){
        for(Player player : lobby.getPlayers()){
            try {
                player.getSession().sendMessage(new TextMessage(mapper.writeValueAsString(new Message("lastFinished",lobby))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void nextRound(Lobby lobby){
        for(Player player : lobby.getPlayers()){
            try {
                player.getSession().sendMessage(new TextMessage(mapper.writeValueAsString(new Message("nextRound", lobby))));
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    public void finishGame(Lobby lobby){
        for(Player player : lobby.getPlayers()){
            try {
                player.getSession().sendMessage(new TextMessage(mapper.writeValueAsString(new Message("finished", lobby))));
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }



}
