package htl.kaindorf.StadtLandFluss.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class Round {
    String letter;
    HashMap<String, List<String>> answers;
}
