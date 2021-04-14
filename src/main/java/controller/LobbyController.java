package controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pojos.Player;


@RestController
public class LobbyController {

    @PostMapping("/enterUsername")
    public Player addPlayer(@RequestBody String name){
        Player player = new Player(name);
        return player;
    }



}
