package PAT.GAMEDLE.controller;

import PAT.GAMEDLE.model.ScoreRequest;
import PAT.GAMEDLE.service.ScoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    @PostMapping("api/score/wordle")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> scoreWordle(@RequestBody @Valid ScoreRequest request)
    {
        scoreService.checkSaveScore(request);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
