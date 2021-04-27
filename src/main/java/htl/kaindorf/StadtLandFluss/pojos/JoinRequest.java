package htl.kaindorf.StadtLandFluss.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinRequest {
    private String name;
    private String code;
}
