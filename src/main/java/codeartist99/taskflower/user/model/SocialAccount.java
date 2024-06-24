package codeartist99.taskflower.user.model;

import codeartist99.taskflower.security.model.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "SocialAccount")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SocialAccount_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "sub_id")
    private String subId;

    @Column(name = "username")
    private String username;

    @Column(name = "provider")
    private String provider;

    public void update(OAuth2UserInfo oAuth2UserInfo) {
        this.subId = oAuth2UserInfo.getSubId();
        this.username = oAuth2UserInfo.getEmail();
    }
}
