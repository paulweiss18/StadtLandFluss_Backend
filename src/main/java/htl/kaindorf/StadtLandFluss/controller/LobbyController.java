package htl.kaindorf.StadtLandFluss.controller;

import htl.kaindorf.StadtLandFluss.pojos.Lobby;
import htl.kaindorf.StadtLandFluss.pojos.Player;
import htl.kaindorf.StadtLandFluss.service.LobbyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private LobbyService lobbyService = new LobbyService();

    @PostMapping("/createLobby")
    public Lobby createdLobby(@RequestBody String name){
        System.out.println("Lobby created");
        return lobbyService.createLobby(new Player(name));
    }






}
