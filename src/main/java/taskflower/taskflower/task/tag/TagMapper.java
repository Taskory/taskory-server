package taskflower.taskflower.task.tag;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag convertTagDtoToTag(TagDto tagDto);

    TagDto converTagToTagDto(Tag tag);
}
