package codeartitect.taskflower.user.model;

import codeartitect.taskflower.security.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity(name = "SocialAccount")
@Getter
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SocialAccount_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "sub_id")
    private String subId;

    @Column(name = "username")
    private String username;

    public void update(OAuth2UserInfo oAuth2UserInfo) {
        this.subId = oAuth2UserInfo.getSucId();
        this.username = oAuth2UserInfo.getEmail();
    }
}
