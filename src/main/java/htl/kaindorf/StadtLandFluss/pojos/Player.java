package htl.kaindorf.StadtLandFluss.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private WebSocketSession session;
}
