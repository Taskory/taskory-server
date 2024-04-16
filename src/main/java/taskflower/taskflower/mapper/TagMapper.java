package taskflower.taskflower.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskflower.taskflower.model.entity.Tag;
import taskflower.taskflower.model.dto.TagDto;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    Tag convertTagDtoToTag(TagDto tagDto);

    TagDto converTagToTagDto(Tag tag);
}
