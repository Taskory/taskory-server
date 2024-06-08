package codeartitect.taskflower.event.payload;

import codeartitect.taskflower.Tag.model.Tag;
import codeartitect.taskflower.hashtag.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveEventRequest {
    private String title;
    private Tag tag;
    private Set<Hashtag> hashtags;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime dueDateTime;
    private String location;
}
