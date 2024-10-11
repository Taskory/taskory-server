package codeartist99.taskflower.tag.payload;

import codeartist99.taskflower.tag.model.Tag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TagResponse {
    private Long id;
    private String title;
    private String color;
    public TagResponse(Tag tag) {
        this.id = tag.getId();
        this.title = tag.getTitle();
        this.color = tag.getColor().name();
    }
}
