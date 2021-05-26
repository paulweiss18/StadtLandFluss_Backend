package htl.kaindorf.StadtLandFluss.websockets.message;

import org.springframework.web.socket.WebSocketMessage;

public class Message implements WebSocketMessage {

    @Override
    public Object getPayload() {
        return null;
    }

    @Override
    public int getPayloadLength() {
        return 0;
    }

    @Override
    public boolean isLast() {
        return false;
    }

}
