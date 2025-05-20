package PAT.GAMEDLE.tareas;

import PAT.GAMEDLE.entity.Words;
import PAT.GAMEDLE.repository.WordsRepository;

import PAT.GAMEDLE.service.WordService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GameScheduler {

    @Autowired
    public WordService wordService;


    private Logger logger = LoggerFactory.getLogger(GameScheduler.class);

    private String palabraDelDia = "INICIAL";

    @Scheduled(cron = "0 1 0 * * *")
    public void actualizarPalabraDelDia() {

        wordService.wordle();
        logger.info("palabra del día{}", wordService.getWordle(null));
    }

    @PostConstruct
    public void inicializarPalabraDelDia() {
        wordService.wordle();
    }

}
