package taskflower.taskflower.model.entity;

import jakarta.persistence.*;
import lombok.*;
import taskflower.taskflower.model.enums.SocialProvider;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SocialAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    Primary id of OAuth2 provider
    @Column
    private String subId;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

//    user ID or Email of OAuth2 provider
    @Column(nullable = false)
    private String username;

    @Column
    private String name;

}
