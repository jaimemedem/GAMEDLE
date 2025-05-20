package PAT.GAMEDLE.service;

import PAT.GAMEDLE.entity.Words;
import PAT.GAMEDLE.model.WordleResponse;
import PAT.GAMEDLE.repository.WordsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class WordServiceTest {

    @Mock
    private WordsRepository wordsRepository;

    @Spy
    @InjectMocks
    private WordService wordService;

    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        // Aseguramos que fetchWordleWord devuelva siempre "prueba"
        lenient().doReturn("prueba").when(wordService).fetchWordleWord();
    }

    @Test
    void whenWordleRuns_andNoExistingEntry_thenSavesNew() {
        // dado que no hay palabra para hoy
        given(wordsRepository.findByDate(today)).willReturn(null);

        // cuando
        wordService.wordle();

        // entonces: no hizo delete, pero sí save con date=today y wordle_word="prueba"
        then(wordsRepository).should(never()).delete(any(Words.class));
        then(wordsRepository).should().save(argThat(day ->
                day.date.equals(today) &&
                        "prueba".equals(day.wordle_word)
        ));
    }

    @Test
    void whenWordleRuns_andExistingEntry_thenDeletesAndSaves() {
        // dado que ya hay palabra para hoy
        Words existing = new Words();
        existing.id = "42L";
        existing.date = today;
        existing.wordle_word = "vieja";
        given(wordsRepository.findByDate(today)).willReturn(existing);

        // cuando
        wordService.wordle();

        // entonces: borra la antigua y guarda la nueva
        then(wordsRepository).should().delete(existing);
        then(wordsRepository).should().save(argThat(day ->
                day.date.equals(today) &&
                        "prueba".equals(day.wordle_word)
        ));
    }

    @Test
    void getWordle_withNullDate_returnsToday() {
        Words stored = new Words();
        stored.id = "1L";
        stored.date = today;
        stored.wordle_word = "hoy";
        given(wordsRepository.findByDate(today)).willReturn(stored);

        WordleResponse resp = wordService.getWordle(null);

        assertThat(resp.word()).isEqualTo("hoy");
    }

    @Test
    void getWordle_withSpecificDate_returnsThat() {
        LocalDate d = LocalDate.of(2025, 1, 1);
        Words stored = new Words();
        stored.id = "2L";
        stored.date = d;
        stored.wordle_word = "anoNuevo";
        given(wordsRepository.findByDate(d)).willReturn(stored);

        WordleResponse resp = wordService.getWordle(d);

        assertThat(resp.word()).isEqualTo("anoNuevo");
    }

    @Test
    void changeWordle_replacesTodayWord_andReturnsResponse() {
        Words stored = new Words();
        stored.id = "3L";
        stored.date = today;
        stored.wordle_word = "antigua";
        given(wordsRepository.findByDate(today)).willReturn(stored);

        WordleResponse resp = wordService.changeWordle("nueva");

        // debería borrar por id
        then(wordsRepository).should().deleteById("3L");
        // y luego guardar la entidad modificada
        then(wordsRepository).should().save(argThat(w ->
                w.date.equals(today) &&
                        "nueva".equals(w.wordle_word)
        ));
        assertThat(resp.word()).isEqualTo("nueva");
    }
}
