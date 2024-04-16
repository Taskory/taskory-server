package taskflower.taskflower.user;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskflower.taskflower.mapper.UserMapper;
import taskflower.taskflower.model.dto.ProfileResponse;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.model.dto.UserDto;
import taskflower.taskflower.model.entity.SocialAccount;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-14T17:30:36+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto convertUserToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setName( user.getName() );
        userDto.setEmail( user.getEmail() );
        Set<SocialAccount> set = user.getSocialAccounts();
        if ( set != null ) {
            userDto.setSocialAccounts( new LinkedHashSet<SocialAccount>( set ) );
        }

        return userDto;
    }

    @Override
    public ProfileResponse convertUserToProfileResponse(User user) {
        if ( user == null ) {
            return null;
        }

        ProfileResponse profileResponse = new ProfileResponse();

        profileResponse.setSocialProviders( extractSocialProviders( user.getSocialAccounts() ) );
        profileResponse.setName( user.getName() );
        profileResponse.setEmail( user.getEmail() );

        return profileResponse;
    }
}
