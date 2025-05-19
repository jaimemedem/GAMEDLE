package PAT.GAMEDLE.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class GameScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    public AppUser user;

    @Column
    public LocalDate playedAt;

    @Column
    public Integer attempts_wordle;

    @Column
    public Boolean success_wordle;

}




