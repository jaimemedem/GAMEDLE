package PAT.GAMEDLE.entity;

import jakarta.persistence.*;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDate;

@Entity
public class Words {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column
    public LocalDate date;

    @Column
    public String wordle_word;


}
