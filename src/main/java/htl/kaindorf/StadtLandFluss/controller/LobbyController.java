package htl.kaindorf.StadtLandFluss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import htl.kaindorf.StadtLandFluss.exception.LobbyJoinException;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.service.LobbyService;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import htl.kaindorf.StadtLandFluss.websockets.SocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/lobby")
@CrossOrigin(origins = "*",methods = {RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class LobbyController {

    private LobbyService lobbyService = new LobbyService();

    @PostMapping("/createPlayer")
    public ResponseEntity createPlayer(@RequestBody String name) {
        try {
            Map<String, String> valueMap = new ObjectMapper().readValue(name, LinkedHashMap.class);

            return ResponseEntity.ok().body(lobbyService.createPlayer(valueMap.get("name")));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An Error occurred - cannot create User");
        }


    }

    @PostMapping("/getLobby")
    public ResponseEntity getLobbyWithCode(@RequestBody String code){

        try {
            Map<String, String> valueMap = new ObjectMapper().readValue(code, LinkedHashMap.class);
            return ResponseEntity.ok(lobbyService.getLobbyByCode(valueMap.get("code")));

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("An Error occurred - cannot get Lobby");
        }

    }

    @PostMapping("/createLobby")
    public ResponseEntity createdLobby(@RequestBody String playerId){
        try {
            Map<String, String> valueMap = new ObjectMapper().readValue(playerId, LinkedHashMap.class);

            return ResponseEntity.ok().body(lobbyService.createLobby(LobbyStorage.getInstance().getPlayerObjById(valueMap.get("playerId"))));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An Error occurred - cannot create a Lobby");
        }
    }


    @PostMapping("/joinLobby")
    public ResponseEntity joinLobby(@RequestBody String joinRequest){
        try{
            Map<String, String> valueMap = new ObjectMapper().readValue(joinRequest, LinkedHashMap.class);

            Lobby lobby = lobbyService.joinLobby(LobbyStorage.getInstance().getPlayerObjById(valueMap.get("playerId")), valueMap.get("lobbyCode"));

            return ResponseEntity.ok().body(lobby);

        }catch(JsonProcessingException ex){
            return ResponseEntity.badRequest().body("An Error occurred - cannot read values");
        }catch (LobbyJoinException ex){
            return ResponseEntity.badRequest().body(ex.toString());
        }
    }


    @PostMapping("/setConfiguration")
    public ResponseEntity changeGameSettings(@RequestBody String configuration){
        try {
            Map<String, String> valueMap = new ObjectMapper().readValue(configuration, LinkedHashMap.class);
            Lobby lobby = lobbyService.configureGameSettings(valueMap.get("playerId"),
                                                             valueMap.get("lobbyCode"),
                                                             Integer.parseInt(valueMap.get("numberOfRounds")),
                                                             Arrays.asList(valueMap.get("categories").split(";")),
                                                             Arrays.asList(valueMap.get("excludedLetters").split(";")));
            if(lobby != null){
                return ResponseEntity.ok().body(lobby);
            }else{
                return ResponseEntity.badRequest().body("Invalid User - only Lobby Leader");
            }


        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("An Error occurred - cannot apply changes");
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body("An Error occurred - cannot apply changes");
        }

    }

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
