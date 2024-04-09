package taskflower.taskflower.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.model.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "socialAccount", defaultExpression = "java( java.util.Collections.emptySet() )")
    UserDto convertUserToUserDto(User user);

}
