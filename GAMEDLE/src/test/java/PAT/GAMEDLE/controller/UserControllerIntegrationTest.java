package PAT.GAMEDLE.controller;

import PAT.GAMEDLE.model.Role;
import PAT.GAMEDLE.repository.AppUserRepository;
import PAT.GAMEDLE.repository.TokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class P5ApplicationE2ETest {

    private static final String NAME = "Name";
    private static final String EMAIL = "name@email.com";
    private static final String PASS = "aaaaaaA1";

    @Autowired
    TestRestTemplate client;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    TokenRepository tokenRepository;



    @Test public void registerTest() {
        // Given ...
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String registro = "{" +
                "\"name\":\"" + NAME + "\"," +
                "\"email\":\"" + EMAIL + "\"," +
                "\"role\":\"" + Role.USER + "\"," +
                "\"password\":\"" + PASS + "\"}";

        // When ...
        ResponseEntity<String> response = client.exchange(
                "http://localhost:8080/api/users",
                HttpMethod.POST, new HttpEntity<>(registro, headers), String.class);

        // Then ...
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("{" +
                        "\"name\":\"" + NAME + "\"," +
                        "\"email\":\"" + EMAIL + "\"," +
                        "\"role\":\"" + Role.USER + "\"}",
                response.getBody());
    }

    /**
     * TODO#11
     * Completa el siguiente test E2E para que verifique la
     * respuesta de login cuando se proporcionan credenciales correctas
     */
    @Test public void loginOkTest() {

        //No puede hacerse este test, por lo menos no como está hecho el código por ahora
        //El registro no guarda al usuario en ningún lado por ahora
        //El login solo puede devolver error de authentication, eso es lo que compruebo


        // Given ...
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String registro = "{" +
                "\"name\":\"" + NAME + "\"," +
                "\"email\":\"" + EMAIL + "\"," +
                "\"role\":\"" + Role.USER + "\"," +
                "\"password\":\"" + PASS + "\"}";

        // When ...
        ResponseEntity<String> response = client.exchange(
                "http://localhost:8080/api/users",
                HttpMethod.POST, new HttpEntity<>(registro, headers), String.class);


        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        String login = "{" +
                "\"email\":\"" + EMAIL + "\"," +
                "\"password\":\"" + PASS + "\"}";
        // When ...
        ResponseEntity<Void> response1 = client.exchange(
                "http://localhost:8080/api/users/me/session",
                HttpMethod.POST, new HttpEntity<>(login, header), Void.class);


        // Then ...
        Assertions.assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        Assertions.assertNull(response1.getBody());
    }
}
