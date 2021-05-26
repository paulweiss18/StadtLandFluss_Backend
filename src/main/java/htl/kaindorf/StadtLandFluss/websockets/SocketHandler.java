package htl.kaindorf.StadtLandFluss.websockets;


import htl.kaindorf.StadtLandFluss.pojos.Player;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    List sessions = new CopyOnWriteArrayList<>();


    //message should contain user-id
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        System.out.println("hello handle message");
        System.out.println(message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
    }

    public void updateLobbyAfterUserJoined(List<Player> players){
        for(Player player : players){
            //player.getSession().sendMessage();
        }
    }


}
