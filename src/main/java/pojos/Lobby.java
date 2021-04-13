package pojos;


import lombok.Data;

import java.util.List;

@Data
public class Lobby {
    private String lobbyCode;
    private List<Player> players;
}
