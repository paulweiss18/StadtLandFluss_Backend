package htl.kaindorf.StadtLandFluss.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class Round {
    String letter;
    HashMap<Player, List<String>> answers;
}
