package codeartitect.taskflower.user.model;

import jakarta.persistence.*;

@Entity(name = "SocialAccount")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SocialAccount_id")
    private Long id;
}
