package taskflower.taskflower.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taskflower.taskflower.security.model.OAuth2UserInfo;
import taskflower.taskflower.model.entity.SocialAccount;
import taskflower.taskflower.repository.SocialAccountRepository;

@Service
public class SocialAccountService {

    private final SocialAccountRepository socialAccountRepository;

    @Autowired
    public SocialAccountService(SocialAccountRepository socialAccountRepository) {
        this.socialAccountRepository = socialAccountRepository;
    }

    public SocialAccount updateSocialAccount(OAuth2UserInfo oAuth2UserInfo) {
        if (socialAccountRepository.existsByUsername(oAuth2UserInfo.getEmail())) {
            SocialAccount socialAccount = socialAccountRepository.findBySubId(oAuth2UserInfo.getSubId());
            socialAccount.setUsername(oAuth2UserInfo.getEmail());
            return socialAccountRepository.save(socialAccount);
        } else throw new UsernameNotFoundException("Your social account is not connected with this service.");
    }
}
