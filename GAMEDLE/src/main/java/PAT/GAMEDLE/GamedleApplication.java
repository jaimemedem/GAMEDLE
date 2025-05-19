package PAT.GAMEDLE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GamedleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamedleApplication.class, args);
	}

}
