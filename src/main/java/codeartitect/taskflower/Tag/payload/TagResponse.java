package codeartitect.taskflower.Tag.payload;

import codeartitect.taskflower.Tag.model.Color;
import codeartitect.taskflower.Tag.model.Tag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TagResponse {
    private Long id;
    private String title;
    private Color color;
    public TagResponse(Tag tag) {
        this.id = tag.getId();
        this.title = tag.getTitle();
        this.color = tag.getColor();
    }
}
