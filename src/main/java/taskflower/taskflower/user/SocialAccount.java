package taskflower.taskflower.user;

import jakarta.persistence.*;
import lombok.*;
import taskflower.taskflower.global.BaseTimeEntity;

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

    @Column
    private String subId;

    @ManyToOne
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    @Column
    private String username;

}
