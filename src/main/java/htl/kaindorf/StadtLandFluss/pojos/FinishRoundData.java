package htl.kaindorf.StadtLandFluss.pojos;

import lombok.Data;

import java.util.List;

@Data
public class FinishRoundData {
    String playerId;
    String lobbyCode;
    List<String> answers;
}
