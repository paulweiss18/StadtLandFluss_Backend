package htl.kaindorf.StadtLandFluss.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LobbyController {

    @PostMapping("/createLobby")
    public String createLobbyButton(){
        return "createLobbyPreset";
    }



}
