package taskflower.taskflower.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import taskflower.taskflower.security.UserPrincipal;
import taskflower.taskflower.security.oauth2.model.GoogleOAuth2UserInfo;
import taskflower.taskflower.security.oauth2.model.OAuth2UserInfo;
import taskflower.taskflower.user.*;
import taskflower.taskflower.user.SocialAccount.SocialAccount;
import taskflower.taskflower.user.SocialAccount.SocialAccountRepository;
import taskflower.taskflower.user.SocialAccount.SocialAccountService;
import taskflower.taskflower.user.SocialAccount.SocialProvider;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialAccountRepository socialAccountRepository;
    private final UserService userService;
    private final SocialAccountService socialAccountService;

    @Autowired
    public CustomOAuth2UserService(SocialAccountRepository socialAccountRepository, UserService userService, SocialAccountService socialAccountService) {
        this.socialAccountRepository = socialAccountRepository;
        this.userService = userService;
        this.socialAccountService = socialAccountService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String socialProvider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo;

//        google 말고도 apple 등 다른 social 연동 정보 등록 예정
        if (socialProvider.equalsIgnoreCase(SocialProvider.GOOGLE.toString())) {
            oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        } else throw new OAuth2AuthenticationException("Does not exist connected social provider info");

        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationException("Your account is not signed up");
        }


        if (socialAccountRepository.existsByUsername(oAuth2UserInfo.getEmail())) {
            SocialAccount socialAccount = socialAccountService.updateSocialAccount(oAuth2UserInfo);
            User user = socialAccount.getUser();
            return new UserPrincipal(user, oAuth2User.getAttributes());
        } else {
            User tempUser = userService.signupTempUserWithOAuth2(oAuth2UserInfo, socialProvider);
            return new UserPrincipal(tempUser, oAuth2User.getAttributes());
        }
    }

}
