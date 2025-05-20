package PAT.GAMEDLE.service;


import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.entity.GameScore;
import PAT.GAMEDLE.model.ScoreRequest;
import PAT.GAMEDLE.model.ScoreResponse;
import PAT.GAMEDLE.model.StatsResponse;
import PAT.GAMEDLE.repository.AppUserRepository;
import PAT.GAMEDLE.repository.GameScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public List<ScoreResponse> getAllScores(AppUser appUser, String game) {
        List<GameScore> scores= gameScoreRepository.findByUser(appUser);
        List<ScoreResponse> responses= new ArrayList<>();
        for (GameScore aux : scores)
        {
            ScoreResponse res = new ScoreResponse(appUser.name,game,aux.attempts_wordle,aux.success_wordle);
            responses.add(res);
        }

        return responses;

    }



    public StatsResponse getStats(String game, AppUser appUser) {

        List<GameScore> scores = gameScoreRepository.findByUser(appUser);
        Integer attempts =  0;
        Integer total_success = 0;
        for (GameScore score: scores){
            attempts+=score.attempts_wordle;
            if(score.success_wordle)
            {
                total_success +=1;
            }
        }
        Float attempts_pg = (float) (attempts/scores.toArray().length);
        Float success_rate = (float) (total_success/scores.toArray().length);
        return new StatsResponse(appUser.name, game,attempts_pg,success_rate);

    }

    public List<StatsResponse> ordenarPorSuccessRate(List<StatsResponse> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (lista.get(j).success_rate().compareTo(lista.get(j + 1).success_rate()) < 0) {
                    StatsResponse temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
        return lista;
    }


            public List<StatsResponse> getLeaderBoard(String game) {
        List<StatsResponse> stats = new ArrayList<>();
        List<AppUser> users = (List<AppUser>) appUserRepository.findAll();
        for (AppUser user : users)
        {
            stats.add(getStats(game,user));
        }

        return stats;

    }
}
