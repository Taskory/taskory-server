package taskflower.taskflower.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskflower.taskflower.model.dto.TagDto;
import taskflower.taskflower.model.entity.Tag;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-25T17:26:59+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public Tag convertTagDtoToTag(TagDto tagDto) {
        if ( tagDto == null ) {
            return null;
        }

        Tag tag = new Tag();

        tag.setId( tagDto.getId() );
        tag.setName( tagDto.getName() );

        return tag;
    }

    @Override
    public TagDto converTagToTagDto(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagDto tagDto = new TagDto();

        tagDto.setId( tag.getId() );
        tagDto.setName( tag.getName() );

        return tagDto;
    }
}
