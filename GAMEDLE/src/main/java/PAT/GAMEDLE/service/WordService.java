package PAT.GAMEDLE.service;

import PAT.GAMEDLE.entity.Words;
import PAT.GAMEDLE.repository.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordService {

    @Autowired
    WordsRepository wordsRepository;

    private static final Set<String> TAGS_VALIDOS = Set.of("n", "adj", "v");
    private static final String API_URL = "https://api.datamuse.com/words?sp=?????&max=100&md=p&v=es";

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchWordleWord() {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(API_URL, List.class);
            List<Map<String, Object>> entries = response.getBody();
            if (entries == null || entries.isEmpty()) {
                return fallback();
            }

            List<String> candidatas = entries.stream()
                    .filter(entry -> entry.containsKey("tags") && entry.get("tags") instanceof List)
                    .filter(entry -> {
                        List<String> tags = (List<String>) entry.get("tags");
                        return tags.stream().map(String::toLowerCase).anyMatch(TAGS_VALIDOS::contains);
                    })
                    .map(entry -> ((String) entry.get("word")).toLowerCase(Locale.ROOT))
                    .collect(Collectors.toList());

            if (candidatas.isEmpty()) {
                return fallback();
            }

            Collections.shuffle(candidatas);
            return candidatas.get(0);

        } catch (Exception e) {
            return fallback();
        }
    }

    private String fallback() {
        return "gatos";

    public void Words()
    {
        Words day =new Words();
        day.date= LocalDate.now();
        day.wordle_word=fetchWordleWord();

        wordsRepository.save(day);
    }


}