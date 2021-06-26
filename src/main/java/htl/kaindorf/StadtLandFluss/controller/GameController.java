package htl.kaindorf.StadtLandFluss.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import htl.kaindorf.StadtLandFluss.pojos.FinishRoundData;
import htl.kaindorf.StadtLandFluss.service.GameService;
import htl.kaindorf.StadtLandFluss.service.LobbyService;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "*",methods = {RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class GameController {

    private SocketHandler socketHandler = new SocketHandler();
    private LobbyService lobbyService = new LobbyService();
    private GameService gameService = new GameService(socketHandler);

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


    @PostMapping("/finishRound")
    public ResponseEntity finishRound(@RequestBody String data){

        try {
            FinishRoundData roundData = new ObjectMapper().readValue(data, FinishRoundData.class);
            gameService.finishRound(roundData.getLobbyCode(), roundData.getPlayerId(), roundData.getAnswers());

            return ResponseEntity.ok().body("Round finished");

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("False Data");
        }








    }

}
