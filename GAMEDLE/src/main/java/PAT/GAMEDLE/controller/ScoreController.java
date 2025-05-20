package PAT.GAMEDLE.controller;

import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.model.ScoreRequest;
import PAT.GAMEDLE.model.ScoreResponse;
import PAT.GAMEDLE.model.StatsResponse;
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
    //cuando se complete el wordle, se hace un post para que el servidor guarde tus estadísticas
    public ResponseEntity<Void> scoreWordle(@CookieValue(value = "session", required = true) String session, @Valid @RequestBody ScoreRequest request)
    {
        AppUser user= userService.authentication(session);
        scoreService.checkSaveScore(request,user);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("api/{game}/scores")
    @ResponseStatus(HttpStatus.OK)
    //para ver tus resultados como jugador
    public List<ScoreResponse> getMyScores(@CookieValue(value = "session", required = true) String session, @PathVariable String game)
    {

        AppUser appUser = userService.authentication(session);
        List<ScoreResponse>scores=scoreService.getAllScores(appUser,game);

        return scores;
    }

    @GetMapping("api/{game}/stats")
    @ResponseStatus(HttpStatus.OK)
    //ver tus estadísticas y promedios como jugador
    public StatsResponse getMyStats(@CookieValue(value = "session", required = true) String session, @PathVariable String game)
    {
        return scoreService.getStats(game, userService.authentication(session));
    }

    @GetMapping("api/{game}/stats/leaderboard")
    @ResponseStatus(HttpStatus.OK)
    //Ver las mejores estadíasticas
    public List<StatsResponse> getLeaderBoard(@PathVariable String game)
    {
        return scoreService.getLeaderBoard(game);
    }




}
