package htl.kaindorf.StadtLandFluss.pojos;


import htl.kaindorf.StadtLandFluss.logic.GamePlay;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Lobby {
    private String lobbyCode;
    private List<Player> players;
    private LobbyStatus status;
    private Player lobbyLeaderPlayer;
    private GameConfiguration gameConfiguration;
    private GamePlay gamePlay;
}
