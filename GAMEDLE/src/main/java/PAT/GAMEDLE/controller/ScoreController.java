package PAT.GAMEDLE.controller;

import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.model.ScoreRequest;
import PAT.GAMEDLE.model.ScoreResponse;
import PAT.GAMEDLE.service.ScoreService;
import PAT.GAMEDLE.service.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScoreController {

    @Autowired
    UserService userService;

    @Autowired
    ScoreService scoreService;

    @PostMapping("api/score/wordle")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> scoreWordle(@Valid @RequestBody  ScoreRequest request)
    {
        scoreService.checkSaveScore(request);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("api/scores/{game}")
    @ResponseStatus(HttpStatus.OK)
    public List<ScoreResponse> getMyScores(@CookieValue(value = "session", required = true) String session, @PathVariable String game)
    {

        AppUser appUser = userService.authentication(session);

        List<ScoreResponse>scores=scoreService.getAllScores(appUser,game);



        return null;
    }

}
