package htl.kaindorf.StadtLandFluss.websockets.message;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketMessage;

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
