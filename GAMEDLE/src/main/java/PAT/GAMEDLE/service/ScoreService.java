package PAT.GAMEDLE.service;


import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.entity.GameScore;
import PAT.GAMEDLE.model.ScoreRequest;
import PAT.GAMEDLE.repository.AppUserRepository;
import PAT.GAMEDLE.repository.GameScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScoreService {
    @Autowired
    GameScoreRepository gameScoreRepository;

    @Autowired
    AppUserRepository appUserRepository;


    public void checkSaveScore(ScoreRequest request)
    {

        List<GameScore> aux= gameScoreRepository.findByUser(appUserRepository.findByName(request.user()));
        Boolean exists= false;
        for (GameScore score : aux)
        {
            if(score.playedAt.equals(LocalDate.now())){
                exists=true;
                if(request.gamename().equals("wordle"))
                {
                    gameScoreRepository.delete(score);
                    score.attempts_wordle= request.attempts();
                    if(request.attempts().equals(5))
                    {
                        score.success_wordle=false;
                    }else{
                        score.success_wordle=true;
                    }
                }
            }
        }

        if(!exists)
        {
            GameScore score = new GameScore();
            score.user= appUserRepository.findByName(request.user());
            score.playedAt=LocalDate.now();
            if(request.gamename().equals("wordle"))
            {
                score.attempts_wordle=request.attempts();
                if(score.attempts_wordle==5)
                {
                    score.success_wordle=false;
                }else{
                    score.success_wordle=true;
                }
            }
        }
    }

}
