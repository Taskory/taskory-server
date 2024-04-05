package taskflower.taskflower.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import taskflower.taskflower.security.UserPrincipal;
import taskflower.taskflower.user.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialAccountRepository socialAccountRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(SocialAccountRepository socialAccountRepository,
                                   UserRepository userRepository) {
        this.socialAccountRepository = socialAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String socialProvider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo;

//        google 말고도 apple 등 다른 social 연동 정보 등록 예정
        if (socialProvider.equalsIgnoreCase(SocialProvider.google.toString())) {
            oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        } else throw new OAuth2AuthenticationException("Does not exist connected social provider info");

        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationException("Your account is not signed up");
        }

        if (socialAccountRepository.existsByUsername(oAuth2UserInfo.getEmail())) {
            updateSocialAccount(oAuth2UserInfo);
            SocialAccount socialAccount = socialAccountRepository.findBySubId(oAuth2UserInfo.getSubId());
            User user = socialAccount.getUser();
            return new UserPrincipal(user);
        } else {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                saveSocialAccount(oAuth2UserInfo, socialProvider);
                return (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            } else throw new UsernameNotFoundException("You can use this service if sign up for");
        }
    }

    private void saveSocialAccount(OAuth2UserInfo oAuth2UserInfo, String socialProvider) {
        SocialAccount socialAccount = new SocialAccount();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByEmail(userDetails.getUsername());
        socialAccount.setUser(user);
        socialAccount.setProvider(SocialProvider.valueOf(socialProvider));
        socialAccount.setSubId(oAuth2UserInfo.getSubId());
        socialAccount.setUsername(oAuth2UserInfo.getEmail());
        socialAccountRepository.save(socialAccount);
    }

    private void updateSocialAccount(OAuth2UserInfo oAuth2UserInfo) {
        SocialAccount socialAccount = socialAccountRepository.findBySubId(oAuth2UserInfo.getSubId());
        socialAccount.setUsername(oAuth2UserInfo.getEmail());
        socialAccountRepository.save(socialAccount);
    }
}
