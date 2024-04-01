package taskflower.taskflower.task.tag;

import org.mapstruct.Mapper;
import taskflower.taskflower.task.tag.model.Tag;
import taskflower.taskflower.task.tag.model.TagDto;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag convertTagDtoToTag(TagDto tagDto);

    TagDto converTagToTagDto(Tag tag);
}
