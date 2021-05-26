package htl.kaindorf.StadtLandFluss.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private String userid;
    private String username;
    private WebSocketSession session;
}
