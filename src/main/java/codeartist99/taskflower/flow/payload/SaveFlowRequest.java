package codeartist99.taskflower.flow.payload;

import codeartist99.taskflower.hashtag.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveFlowRequest {
    private String title;
    private List<Hashtag> hashtags;
    private String description;
}
