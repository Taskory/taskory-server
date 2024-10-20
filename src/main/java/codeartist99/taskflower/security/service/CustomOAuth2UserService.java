package codeartist99.taskflower.security.service;

import codeartist99.taskflower.security.model.GoogleOAuth2UserInfo;
import codeartist99.taskflower.security.model.OAuth2UserInfo;
import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.user.SocialAccountRepository;
import codeartist99.taskflower.user.UserService;
import codeartist99.taskflower.user.model.SocialAccount;
import codeartist99.taskflower.user.model.SocialProvider;
import codeartist99.taskflower.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Custom service for handling OAuth2 authentication.
 * Extends {@link DefaultOAuth2UserService} to load user information from the OAuth2 provider.
 */
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialAccountRepository socialAccountRepository;
    private final UserService userService;

    /**
     * Constructor to inject dependencies for social account management.
     *
     * @param socialAccountRepository the repository to handle social account data.
     * @param userService the service to manage user operations.
     */
    @Autowired
    public CustomOAuth2UserService(SocialAccountRepository socialAccountRepository, UserService userService) {
        this.socialAccountRepository = socialAccountRepository;
        this.userService = userService;
    }

    /**
     * Loads the user's information from the OAuth2 provider.
     * If the user exists, their information is updated, otherwise a temporary user is registered.
     *
     * @param userRequest the OAuth2UserRequest containing the user's authentication request details.
     * @return an {@link OAuth2User} object with the loaded user information.
     * @throws OAuth2AuthenticationException if the authentication process fails.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[LOG] OAuth2 authentication request received for provider: {}",
                userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("[LOG] OAuth2 user attributes loaded: {}", oAuth2User.getAttributes());

        String socialProvider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo;

        if (socialProvider.equalsIgnoreCase(SocialProvider.GOOGLE.name())) {
            oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
            log.info("[LOG] Google OAuth2 user info extracted.");
        } else {
            log.error("[LOG] Unsupported social provider: {}", socialProvider);
            throw new OAuth2AuthenticationException("Does not exist connected social provider info");
        }

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            log.error("[LOG] OAuth2 user email is empty.");
            throw new OAuth2AuthenticationException("Your account is not signed up");
        }

        log.info("[LOG] Checking if user exists by email: {}", oAuth2UserInfo.getEmail());

        if (socialAccountRepository.existsByUsername(oAuth2UserInfo.getEmail())) {
            log.info("[LOG] User found, updating social account: {}", oAuth2UserInfo.getEmail());
            SocialAccount socialAccount = socialAccountRepository.findByUsername(oAuth2UserInfo.getEmail());
            socialAccount.update(oAuth2UserInfo);
            socialAccountRepository.save(socialAccount);
            log.info("[LOG] Social account updated and saved.");

            User user = socialAccount.getUser();
            log.info("[LOG] Returning UserPrincipal for existing user: {}", user.getUsername());
            return new UserPrincipal(user, oAuth2User.getAttributes());
        } else {
            log.info("[LOG] No existing user found, registering temporary user.");
            User tempUser = userService.registerTempUser(oAuth2UserInfo, socialProvider);
            log.info("[LOG] Temporary user registered: {}", tempUser.getUsername());
            return new UserPrincipal(tempUser, oAuth2User.getAttributes());
        }
    }
}
