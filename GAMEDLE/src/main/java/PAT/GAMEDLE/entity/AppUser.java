package PAT.GAMEDLE.entity;

import PAT.GAMEDLE.model.Role;
import jakarta.persistence.*;

@Entity(name="APP_USER")
@Table(name="APP_USER")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) public String id;

    @Column(nullable = false, unique = true) public String email;

    @Column (nullable = false) public String password;

    @Enumerated(EnumType.STRING) @Column (nullable = false) public Role rol;

    @Column (nullable = false) public String name;
}