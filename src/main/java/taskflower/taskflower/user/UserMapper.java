package taskflower.taskflower.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.model.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User convertUserDtoToUser(UserDto userDto);

    @Mapping(target = "socialAccount", defaultExpression = "java( java.util.Collections.emptySet() )")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDto convertUserToUserDto(User user);

}
