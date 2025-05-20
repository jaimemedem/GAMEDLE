package PAT.GAMEDLE.controller;


import PAT.GAMEDLE.model.Role;
import PAT.GAMEDLE.model.WordleResponse;
import PAT.GAMEDLE.service.UserService;
import PAT.GAMEDLE.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
public class WordController {

    @Autowired
    WordService wordService;

    @Autowired
    UserService userService;

    @GetMapping("api/wordle/today")
    @ResponseStatus(HttpStatus.OK)
    //Cuando empieza el juego se consulta la palabra con el servidor
    public WordleResponse conseguirWordle()
    {
        return wordService.getWordle(null);

    }

    @GetMapping("api/wordle")
    @ResponseStatus(HttpStatus.OK)
    //para consultar palabras de otras fechas
    public WordleResponse getWordleFrom(@RequestBody LocalDate date)
    {
        return wordService.getWordle(date);
    }

    @PutMapping("api/wordle/change")
    //en caso de que el algoritmo que selecciona palabras devuelva una que el administrador no considere, puede cambiarla
    public WordleResponse changeWordle(@CookieValue(value = "session", required = true) String session, @RequestBody String word)
    {
        if(userService.authentication(session).rol.equals(Role.ADMIN))
        {
            return wordService.changeWordle(word);

        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }





}
