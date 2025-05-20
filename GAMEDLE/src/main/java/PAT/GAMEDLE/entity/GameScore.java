package PAT.GAMEDLE.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
public class GameScore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public AppUser user;

    @Column
    public LocalDate playedAt;

    @Column
    public Integer attempts_wordle;

    @Column
    public Boolean success_wordle;

}




