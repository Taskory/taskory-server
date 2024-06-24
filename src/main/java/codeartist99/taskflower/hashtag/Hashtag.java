package codeartist99.taskflower.hashtag;

import jakarta.persistence.*;

@Entity(name = "Hashtag")
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;
}
