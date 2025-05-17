package PAT.GAMEDLE.repository;

import PAT.GAMEDLE.entity.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser,String> {

    AppUser findByEmail(String email);


}
