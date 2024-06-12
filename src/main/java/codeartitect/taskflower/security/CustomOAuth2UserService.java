package codeartitect.taskflower.security;

import codeartitect.taskflower.user.SocialAccountRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.model.SocialAccount;
import codeartitect.taskflower.user.model.SocialProvider;
import codeartitect.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final SocialAccountRepository socialAccountRepository;
    private final UserService userService;

    @Autowired
    public CustomOAuth2UserService(SocialAccountRepository socialAccountRepository, UserService userService) {
        this.socialAccountRepository = socialAccountRepository;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String socialProvider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo;

        if (socialProvider.equalsIgnoreCase(SocialProvider.GOOGLE.name())) {
            oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        } else throw new OAuth2AuthenticationException("Does not exist connected social provider info");

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationException("Your account is not signed up");
        }

        if (socialAccountRepository.existsByUsername(oAuth2UserInfo.getEmail())) {
            SocialAccount socialAccount = socialAccountRepository.findByUsername(oAuth2UserInfo.getEmail());
            socialAccount.update(oAuth2UserInfo);
            socialAccountRepository.save(socialAccount);
            User user = socialAccount.getUser();
            return new UserPrincipal(user, oAuth2User.getAttributes());
        } else {
            User tempUser = userService.registerTempUser(oAuth2UserInfo, socialProvider);
            return new UserPrincipal(tempUser, oAuth2User.getAttributes());
        }
    }
}
