package PAT.GAMEDLE.service;

import PAT.GAMEDLE.entity.Words;
import PAT.GAMEDLE.model.WordleResponse;
import PAT.GAMEDLE.repository.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordService {

    @Autowired
    WordsRepository wordsRepository;

    private final Random random = new Random();
    private static final String RESOURCE_PATH = "dictionary/words.txt";

    public String fetchWordleWord() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PATH)) {

            if (inputStream == null) {
                throw new IllegalStateException("No se encontró el archivo: " + RESOURCE_PATH);
            }

            List<String> fiveLetterWords = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .map(String::trim)
                    .filter(w -> w.length() == 5 && w.matches("[a-zA-ZáéíóúñÑÁÉÍÓÚ]+"))
                    .map(String::toLowerCase)
                    .distinct()
                    .collect(Collectors.toList());

            if (fiveLetterWords.isEmpty()) return "gatos";

            return fiveLetterWords.get(random.nextInt(fiveLetterWords.size()));

        } catch (Exception e) {
            e.printStackTrace();
            return "gatos";
        }
    }


    public void wordle() {

        Words day = new Words();
        day.date = LocalDate.now();
        day.wordle_word = fetchWordleWord();
        Words aux = wordsRepository.findByDate(LocalDate.now());
        if (aux != null) {
            wordsRepository.delete(aux);

        }

        wordsRepository.save(day);
    }

    public WordleResponse getWordle(LocalDate date) {
        if (date == null) {
            Words word = wordsRepository.findByDate(LocalDate.now());
            return new WordleResponse(word.wordle_word);

        } else {
            Words word = wordsRepository.findByDate(date);
            return new WordleResponse(word.wordle_word);

        }
    }

    public WordleResponse changeWordle(String word) {
        Words palabra = wordsRepository.findByDate(LocalDate.now());
        wordsRepository.deleteById(palabra.id);
        palabra.wordle_word=word;
        wordsRepository.save(palabra);

        WordleResponse response = new WordleResponse(palabra.wordle_word);

        return response;

    }


}