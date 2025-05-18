package PAT.GAMEDLE.tareas;

import PAT.GAMEDLE.entity.Words;
import PAT.GAMEDLE.repository.WordsRepository;

import PAT.GAMEDLE.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GameScheduler {

    @Autowired
    public WordService wordService;

    @Autowired
    public WordsRepository wordsRepository;

    private String palabraDelDia = "INICIAL";

    @Scheduled(cron = "0 1 0 * * *")
    public void actualizarPalabraDelDia() {
        wordService.fetchWordleWord();
    }
}
