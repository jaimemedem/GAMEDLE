package PAT.GAMEDLE.controller;


import PAT.GAMEDLE.model.WordleResponse;
import PAT.GAMEDLE.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class WordController {

    @Autowired
    WordService wordService;

    @GetMapping("api/wordle/today")
    @ResponseStatus(HttpStatus.OK)
    public WordleResponse conseguirWordle()
    {
        return wordService.getWordle(null);

    }

    @GetMapping("api/wordle")
    @ResponseStatus(HttpStatus.OK)
    public WordleResponse getWordleFrom(@RequestBody LocalDate date)
    {
        return wordService.getWordle(date);
    }



}
