package taskflower.taskflower.task.tag;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskflower.taskflower.task.tag.model.Tag;
import taskflower.taskflower.task.tag.model.TagDto;

@Mapper(componentModel = "spring")
public interface TagMapper {

//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "user", ignore = true)
    Tag convertTagDtoToTag(TagDto tagDto);

    TagDto converTagToTagDto(Tag tag);
}
