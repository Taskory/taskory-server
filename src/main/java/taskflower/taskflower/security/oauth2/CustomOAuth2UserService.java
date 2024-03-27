package taskflower.taskflower.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import taskflower.taskflower.security.OAuth2AuthenticationProcessingException;
import taskflower.taskflower.security.UserDetailsImpl;
import taskflower.taskflower.user.OAuth2ConnectionInfo;
import taskflower.taskflower.user.OAuth2ConnectionInfoRepository;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserRepository;
import taskflower.taskflower.user.exception.UserNotFoundException;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2ConnectionInfoRepository oAuth2ConnectionInfoRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(OAuth2ConnectionInfoRepository oAuth2ConnectionInfoRepository, UserRepository userRepository) {
        this.oAuth2ConnectionInfoRepository = oAuth2ConnectionInfoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        OAuth2UserDetails oAuth2UserDetails = null;
        try {
            oAuth2UserDetails = OAuth2UserDetailsFactory.getOAuth2UserDetails(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        } catch (OAuth2AuthenticationProcessingException e) {
            throw new UserNotFoundException();
        }

        if (!oAuth2ConnectionInfoRepository.existsByUsername(oAuth2UserDetails.getEmail())) {
            saveOAuth2ConnectionInfo(oAuth2UserRequest, oAuth2UserDetails);
        }

        return getCurrentUserDetails();
    }

    private void saveOAuth2ConnectionInfo(OAuth2UserRequest oAuth2UserRequest, OAuth2UserDetails oAuth2UserDetails) {
        UserDetailsImpl userDetails = getCurrentUserDetails();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotFoundException::new);
        OAuth2ConnectionInfo oAuth2ConnectionInfo = new OAuth2ConnectionInfo();
        oAuth2ConnectionInfo.setUser(user);
        oAuth2ConnectionInfo.setUsername(oAuth2UserDetails.getEmail());
        oAuth2ConnectionInfo.setProviderType(AuthProviderType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        oAuth2ConnectionInfoRepository.save(oAuth2ConnectionInfo);
    }

    private static UserDetailsImpl getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
