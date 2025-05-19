package PAT.GAMEDLE.repository;

import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.entity.GameScore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameScoreRepository extends CrudRepository<GameScore,String> {

    List<GameScore> findByUser(AppUser user);
}
