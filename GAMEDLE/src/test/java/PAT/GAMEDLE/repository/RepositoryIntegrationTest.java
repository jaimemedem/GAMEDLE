package PAT.GAMEDLE.repository;

import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.entity.Token;
import PAT.GAMEDLE.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class RepositoryIntegrationTest {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired AppUserRepository appUserRepository;


    @Test
    void saveTest() {
        // Given ...
        AppUser user = new AppUser();
        user.rol= Role.USER;
        user.name="nombre";
        user.email="nombre@correo.com";
        user.password="aaaaaaaA1";
        Token token = new Token();
        token.appUser=user;
        // When ...
        DataIntegrityViolationException error = null;
        try{
            appUserRepository.save(user);
            tokenRepository.save(token);
            AppUser user1=appUserRepository.findByEmail("nombre@correo.com");
            if(user!=user1){
                appUserRepository.save(null);
            }
            Token token1=tokenRepository.findByAppUser(user);
            if(token1!=token){
                appUserRepository.save(null);
            }
        }catch (DataIntegrityViolationException e) {
            error=e;
        }
        // Then ...
        assertNull(error);
    }


    @Test void deleteCascadeTest() {
        // Given ...
        AppUser user = new AppUser();
        user.rol=Role.USER;
        user.name="nombre";
        user.email="nombre@correo.com";
        user.password="aaaaaaaA1";
        Token token = new Token();
        token.appUser=user;
        appUserRepository.save(user);
        tokenRepository.save(token);
        // When ...
        DataIntegrityViolationException error = null;
        try{
            appUserRepository.deleteById(user.id);

        }catch (DataIntegrityViolationException e) {
            error=e;
        }

    }


    @Test void wordleTest()
    {

    }
}