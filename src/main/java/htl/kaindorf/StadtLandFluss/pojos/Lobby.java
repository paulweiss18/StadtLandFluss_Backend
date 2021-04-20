package htl.kaindorf.StadtLandFluss.pojos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class Lobby {
    private String lobbyCode;
    private List<Player> players;
    private LobbyStatus status;
    private Player lobbyLeaderPlayer;
}
