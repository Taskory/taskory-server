package taskflower.taskflower.user;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskflower.taskflower.user.SocialAccount.SocialAccount;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.model.UserDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-09T18:02:49+0900",
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

        Set<SocialAccount> set = user.getSocialAccount();
        if ( set != null ) {
            userDto.setSocialAccount( new LinkedHashSet<SocialAccount>( set ) );
        }
        else {
            userDto.setSocialAccount( java.util.Collections.emptySet() );
        }
        userDto.setId( user.getId() );
        userDto.setName( user.getName() );
        userDto.setEmail( user.getEmail() );

        return userDto;
    }
}
