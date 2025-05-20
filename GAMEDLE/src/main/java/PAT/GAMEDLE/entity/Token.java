package PAT.GAMEDLE.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    // @ManytoOne(fetch = FetchType.LAZY)
    @OneToOne
    @JoinColumn(name = "USER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public AppUser appUser;

}