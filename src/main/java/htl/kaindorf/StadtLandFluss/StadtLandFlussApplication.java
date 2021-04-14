package htl.kaindorf.StadtLandFluss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import pojos.Player;

@SpringBootApplication
@RestController
public class StadtLandFlussApplication {

	public static void main(String[] args) {
		SpringApplication.run(StadtLandFlussApplication.class, args);
	}


}
