package htl.kaindorf.StadtLandFluss.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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

/**
 * Class GameController - Controller handles all Requests interacting in the Game
 */
@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "*",methods = {RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class GameController {

    private SocketHandler socketHandler = new SocketHandler();
    private LobbyService lobbyService = new LobbyService();
    private GameService gameService = new GameService(socketHandler);

    /**
     * Method to Start a Game, it is only possible when PlayerId = LobbyLeader and Number of Players > 1
     * @Endpoint /startGame
     * @param gameData is cast to value Map, with PlayerId and LobbyCode
     * @return ResponseEntity true or false - whether Lobby is able to start game
     */
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

    /**
     * Method to Finish a Game, when the first Player finished all his inputs - server gets notified
     * @Endpoint /finishRound
     * @param data is cast to a FinishRoundData Object with LobbyCode, PlayerId and a List with Answers
     * @return ResponseEntity true or false - whether Data was successfully parsed
     */
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

    /**
     * Method to Vote for a Round, when Lobby Leader finished Voting for one Player
     * @Endpoint /voteRound
     * @param data JSON String is parsed to PlayerId and Score
     * @return ResponseEntity true or false - whether Data was successfully parsed
     */
    @PostMapping("/voteRound")
    public ResponseEntity voteRound(@RequestBody String data){
        try {
            Map<String, String> valueMap = new ObjectMapper().readValue(data, LinkedHashMap.class);
            String playerId = valueMap.get("playerId");
            String score = valueMap.get("score");
            gameService.votePlayer(playerId, Integer.parseInt(score));

            return ResponseEntity.ok().body("Player Voted");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("False Data");
        }
    }

    @PostMapping("/nextRound")
    public ResponseEntity nextRound(@RequestBody String data){
        try{
            Map<String, String> valueMap = new ObjectMapper().readValue(data, LinkedHashMap.class);
            String lobbyCode = valueMap.get("lobbyCode");
            gameService.nextRound(lobbyCode);

            return ResponseEntity.ok().body("new Round");

        } catch (JsonMappingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("False Data");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("False Data");
        }
    }

}
