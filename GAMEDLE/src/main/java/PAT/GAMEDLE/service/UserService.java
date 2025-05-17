package PAT.GAMEDLE.service;

import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.entity.Token;
import PAT.GAMEDLE.model.ProfileRequest;
import PAT.GAMEDLE.model.ProfileResponse;
import PAT.GAMEDLE.model.RegisterRequest;
import PAT.GAMEDLE.repository.AppUserRepository;
import PAT.GAMEDLE.repository.TokenRepository;
import PAT.GAMEDLE.util.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired
    private AppUserRepository userRepository;
    @Autowired private TokenRepository tokenRepository;
    @Autowired private Hashing hashing;


    public Token login(String email, String password) {
        AppUser appUser = userRepository.findByEmail(email);
        if (appUser == null) return null;
        if (!hashing.compare(appUser.password, password)) return null;
        Token token = tokenRepository.findByAppUser(appUser);
        if (token == null) {
            token = new Token();


            token.appUser = appUser;

            token = tokenRepository.save(token);
        }
        return token;
    }

    public AppUser authentication(String tokenId) {
        Token token = tokenRepository.findById(tokenId).orElse(null);
        if (token==null){

            return null;

        }else {

            return token.appUser;
        }
    }


    public ProfileResponse profile(RegisterRequest register) {
        AppUser user = new AppUser();

        user.name     = register.name();
        user.email    = register.email();
        user.rol      = register.role();
        user.password = hashing.hash(register.password());

        try {

            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {

            throw e;
        }


        return new ProfileResponse(user.name, user.email, user.rol);
    }


    public ProfileResponse profile(AppUser appUser) {
        return new ProfileResponse(appUser.name, appUser.email, appUser.rol);
    }


    public ProfileResponse profile(AppUser appUser, ProfileRequest profile) {
        appUser.name     = profile.name();
        appUser.rol      = profile.role();
        appUser.password = hashing.hash(profile.password());

        userRepository.save(appUser);

        return new ProfileResponse(appUser.name, appUser.email, appUser.rol);
    }


    public void logout(String tokenId) {
        tokenRepository.deleteById(tokenId);
    }


    public void delete(AppUser appUser) {
        userRepository.deleteById(appUser.id);
    }
}
