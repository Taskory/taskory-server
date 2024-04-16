package taskflower.taskflower.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import taskflower.taskflower.model.dto.ProfileResponse;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.model.dto.UserDto;
import taskflower.taskflower.model.entity.SocialAccount;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto convertUserToUserDto(User user);

    @Mapping(source = "socialAccounts", target = "socialProviders", qualifiedByName = "extractSocialProviders")
    ProfileResponse convertUserToProfileResponse(User user);

    @Named("extractSocialProviders")
    default Set<String> extractSocialProviders(Set<SocialAccount> socialAccounts) {
        return socialAccounts.stream()
                .map(SocialAccount::getProvider)
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
