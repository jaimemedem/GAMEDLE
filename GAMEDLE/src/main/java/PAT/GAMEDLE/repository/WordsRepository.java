package PAT.GAMEDLE.repository;

import PAT.GAMEDLE.entity.Words;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface WordsRepository extends CrudRepository<Words,String> {

    Words findByDate(LocalDate date);
}
