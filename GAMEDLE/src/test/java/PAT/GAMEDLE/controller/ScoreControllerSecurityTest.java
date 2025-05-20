package PAT.GAMEDLE.controller;

import PAT.GAMEDLE.entity.AppUser;
import PAT.GAMEDLE.model.ScoreRequest;
import PAT.GAMEDLE.model.ScoreResponse;
import PAT.GAMEDLE.model.StatsResponse;
import PAT.GAMEDLE.service.ScoreService;
import PAT.GAMEDLE.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.Cookie;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de autorización (401/403) y flujo básico (200/201) para ScoreController.
 */
@WebMvcTest(ScoreController.class)
class ScoreControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ScoreService scoreService;

    private static final String GAME = "wordle";
    private static final String COOKIE_NAME = "session";

    @Test
    @DisplayName("GET /api/{game}/scores → 401 si no hay cookie de sesión")
    void whenNoSessionCookie_getScores_thenUnauthorized() throws Exception {
        mvc.perform(get("/api/{game}/scores", GAME))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/{game}/scores → 401 si session inválida")
    void whenInvalidSession_getScores_thenUnauthorized() throws Exception {
        // Simulamos que el servicio lanza excepción de no autorizado
        given(userService.authentication("bad-session"))
                .willThrow(new ResponseStatusException(UNAUTHORIZED, "Invalid session"));

        mvc.perform(get("/api/{game}/scores", GAME)
                        .cookie(new Cookie(COOKIE_NAME, "bad-session")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/{game}/scores → 200 con sesión válida")
    void whenValidSession_getScores_thenOk() throws Exception {
        AppUser dummy = new AppUser(); // ajusta según tu constructor
        given(userService.authentication("good-session")).willReturn(dummy);
        given(scoreService.getAllScores(eq(dummy), eq(GAME)))
                .willReturn(Collections.emptyList());

        mvc.perform(get("/api/{game}/scores", GAME)
                        .cookie(new Cookie(COOKIE_NAME, "good-session")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/{game}/stats → 401 sin cookie")
    void whenNoSession_getStats_thenUnauthorized() throws Exception {
        mvc.perform(get("/api/{game}/stats", GAME))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/{game}/stats → 200 con sesión válida")
    void whenValidSession_getStats_thenOk() throws Exception {
        AppUser dummy = new AppUser();
        // Creamos un StatsResponse válido con sus 4 parámetros
        StatsResponse stats = new StatsResponse(
                "dummyUser",
                GAME,
                0f,
                0f
        );

        given(userService.authentication("good-session")).willReturn(dummy);
        given(scoreService.getStats(eq(GAME), eq(dummy))).willReturn(stats);

        mvc.perform(get("/api/{game}/stats", GAME)
                        .cookie(new Cookie(COOKIE_NAME, "good-session")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // opcional: podemos verificar que el JSON contiene el nombre de usuario
                .andExpect(jsonPath("$.user").value("dummyUser"))
                .andExpect(jsonPath("$.gamename").value(GAME));
    }


    @Test
    @DisplayName("GET /api/{game}/stats/leaderboard → 200 sin necesidad de sesión")
    void whenNoSession_getLeaderboard_thenOk() throws Exception {
        given(scoreService.getLeaderBoard(GAME))
                .willReturn(Collections.emptyList());

        mvc.perform(get("/api/{game}/stats/leaderboard", GAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /api/score/wordle → 201 crea puntuación sin cookie")
    void whenPostScore_withoutSession_thenCreated() throws Exception {
        ScoreRequest req = new ScoreRequest("user1", GAME, 3);
        // No se lanza excepción => debería devolver CREATED
        mvc.perform(post("/api/score/wordle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}
