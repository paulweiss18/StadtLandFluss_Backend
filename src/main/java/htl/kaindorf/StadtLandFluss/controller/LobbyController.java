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

/**
 * Class LobbyController - Controller responsible for any Requests for Lobby changes
 */
@RestController
@RequestMapping("/lobby")
@CrossOrigin(origins = "*",methods = {RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class LobbyController {

    private LobbyService lobbyService = new LobbyService();


    /**
     * Method to create a Player Object - only possible to play when a Player Object has been created
     * @Endpoint /createPlayer
     * @param name Username of Player
     * @return ResponseEntity with Player Object
     */
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

    /**
     * Method to get the Lobby Object by the lobbycode
     * @Endpoint /getLobby
     * @param code LobbyCode of Lobby
     * @return ResponseEntity with Lobby Object
     */
    @PostMapping("/getLobby")
    public ResponseEntity getLobbyWithCode(@RequestBody String code){

        try {
            Map<String, String> valueMap = new ObjectMapper().readValue(code, LinkedHashMap.class);
            return ResponseEntity.ok(lobbyService.getLobbyByCode(valueMap.get("code")));

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("An Error occurred - cannot get Lobby");
        }

    }

    /**
     * Method to create a Lobby
     * @Endpoint /createLobby
     * @param playerId PlayerId is used to identify the LobbyLeader
     * @return ResponseEntity with created Lobby Object
     */
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


    /**
     * Method to join a Lobby
     * @Endpoint /joinLobby
     * @param joinRequest PlayerId and LobbyCode
     * @return ResponseEntity with created Lobby Object
     * @Exception is returned when Lobby isn't available or Game has already started
     */
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

    /**
     * Method to configure the GameSettings of a Lobby
     * @Endpoint /setConfiguration
     * @param configuration NumberOfRounds, ExcludedLetters, Categories as String
     * @return ResponseEntity with created Lobby Object
     */
    @PostMapping("/setConfiguration")
    public ResponseEntity changeGameSettings(@RequestBody String configuration){
        try {
            Map<String, String> valueMap = new ObjectMapper().readValue(configuration, LinkedHashMap.class);
            System.out.println(valueMap.get("numberOfRounds"));
            Lobby lobby = lobbyService.configureGameSettings(valueMap.get("playerId"),
                                                             valueMap.get("lobbyCode"),
                                                             Integer.parseInt(valueMap.get("numberOfRounds")),
                                                             Arrays.asList(valueMap.get("categories").split("\\,")),
                                                             Arrays.asList(valueMap.get("excludedLetters").split("\\,")));
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


}
