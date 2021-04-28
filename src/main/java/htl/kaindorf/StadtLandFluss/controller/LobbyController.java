package htl.kaindorf.StadtLandFluss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import htl.kaindorf.StadtLandFluss.exception.LobbyJoinException;
import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.service.LobbyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private LobbyService lobbyService = new LobbyService();

    @PostMapping("/createLobby")
    public Lobby createdLobby(@RequestBody String name){
        System.out.println("Lobby created");
        return lobbyService.createLobby(new Player(name));
    }

    @PostMapping("/joinLobby")
    public Lobby joinLobby(@RequestBody String joinRequest) throws LobbyJoinException {
        try{
            Map<String, String> valueMap = new ObjectMapper().readValue(joinRequest, LinkedHashMap.class);
            return lobbyService.joinLobby(new Player(valueMap.get("name")), valueMap.get("code"));

        }catch(JsonProcessingException ex){
            System.out.println("invalid");
            return null;
        }
    }






}
