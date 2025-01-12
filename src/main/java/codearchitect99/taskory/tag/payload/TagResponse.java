package codearchitect99.taskory.tag.payload;

import codearchitect99.taskory.tag.model.Tag;
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
