package htl.kaindorf.StadtLandFluss.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class GameConfiguration {
    private int numberOfRounds;
    private List<String> categories;
    private List<String> excludedLetters;
}
