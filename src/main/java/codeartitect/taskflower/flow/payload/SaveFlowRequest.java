package codeartitect.taskflower.flow.payload;

import codeartitect.taskflower.hashtag.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveFlowRequest {
    private String title;
    private List<Hashtag> hashtags;
    private String description;
}
