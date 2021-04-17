package htl.kaindorf.StadtLandFluss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
public class StadtLandFlussApplication {

	public static void main(String[] args) {
		SpringApplication.run(StadtLandFlussApplication.class, args);
	}

	@RequestMapping("/")
	public String startPage(){
		return "startpage";
	}


}
