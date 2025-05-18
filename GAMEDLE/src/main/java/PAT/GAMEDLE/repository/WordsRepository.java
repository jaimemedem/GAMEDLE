package PAT.GAMEDLE.repository;

import PAT.GAMEDLE.entity.Words;
import org.springframework.data.repository.CrudRepository;

public interface WordsRepository extends CrudRepository<Words,String> {
}
