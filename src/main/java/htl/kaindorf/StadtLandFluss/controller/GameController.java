package htl.kaindorf.StadtLandFluss.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import htl.kaindorf.StadtLandFluss.service.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "*",methods = {RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class GameController {

    private LobbyService lobbyService = new LobbyService();

    @PostMapping("/startGame")
    public ResponseEntity startGame(@RequestBody String gameData){
        try {
            Map<String, String> valueMap = new ObjectMapper().readValue(gameData, LinkedHashMap.class);
            if(lobbyService.startGame(valueMap.get("playerId"), valueMap.get("lobbyCode"))){
                return ResponseEntity.ok().body(true);
            }else{
                return ResponseEntity.badRequest().body(false);
            }

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("False GameData");
        }

    }

}
