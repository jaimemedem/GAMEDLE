package PAT.GAMEDLE.repository;

import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {

    Token findByAppUser(AppUser id);
}
