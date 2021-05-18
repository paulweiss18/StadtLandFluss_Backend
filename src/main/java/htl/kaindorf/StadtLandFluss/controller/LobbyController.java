package htl.kaindorf.StadtLandFluss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import htl.kaindorf.StadtLandFluss.exception.LobbyJoinException;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.service.LobbyService;
import htl.kaindorf.StadtLandFluss.storage.LobbyStorage;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/lobby")
@CrossOrigin(origins = "*",methods = {RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class LobbyController {

    private LobbyService lobbyService = new LobbyService();

    @PostMapping("/createLobby")
    public Lobby createLobby(@RequestBody String name){
        System.out.println("Lobby created");
        return lobbyService.createLobby(new Player(name, UUID.randomUUID().toString()));
    }

    @PostMapping("/joinLobby")
    public Lobby joinLobby(@RequestBody String joinRequest) throws LobbyJoinException {
        try{
            Map<String, String> valueMap = new ObjectMapper().readValue(joinRequest, LinkedHashMap.class);
            return lobbyService.joinLobby(new Player(valueMap.get("name"), UUID.randomUUID().toString()), valueMap.get("code"));

        }catch(JsonProcessingException ex){
            System.out.println("invalid");
            return null;
        }
    }


    @PostMapping("/setConfiguration")
    public Lobby changeGameSettings(@RequestBody String configuration){

        Map<String, String> valueMap = null;
        try {
            valueMap = new ObjectMapper().readValue(configuration, LinkedHashMap.class);

            LobbyStorage.getInstance().getLobbies().get(valueMap.get("lobbyCode")).getGameConfiguration().setNumberOfRounds(Integer.parseInt(valueMap.get("numberOfRounds")));
            LobbyStorage.getInstance().getLobbies().get(valueMap.get("lobbyCode")).getGameConfiguration().setCategories(Arrays.asList(valueMap.get("categories").split(";")));
            LobbyStorage.getInstance().getLobbies().get(valueMap.get("lobbyCode")).getGameConfiguration().setExcludedLetters(Arrays.asList(valueMap.get("excludedLetters").split(";")));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("invalid");
            return null;
        }
        return  LobbyStorage.getInstance().getLobbies().get(valueMap.get("lobbyCode"));
    }

}
