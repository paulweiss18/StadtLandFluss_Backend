package htl.kaindorf.StadtLandFluss.websockets.message;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketMessage;

/**
 * Custom Message which is send by WebSockets
 */
@Data
@AllArgsConstructor
public class Message{
    private String type;
    private Object data;


    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
