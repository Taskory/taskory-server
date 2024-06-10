package codeartitect.taskflower.flow.payload;

import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.hashtag.Hashtag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class FlowResponse {
    private Long id;
    private String title;
    private List<Hashtag> hashtags;
    private String description;

    public FlowResponse(Flow flow) {
        this.id = flow.getId();
        this.title = flow.getTitle();
        this.hashtags = flow.getHashtags();
        this.description = flow.getDescription();
    }
}
