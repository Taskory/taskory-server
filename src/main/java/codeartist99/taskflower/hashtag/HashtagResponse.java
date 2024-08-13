package codeartist99.taskflower.hashtag;

import codeartist99.taskflower.user.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HashtagResponse {
    private Long id;
    private String title;
    private User user;

    public HashtagResponse(Hashtag hashtag) {
        this.id = hashtag.getId();
        this.title = hashtag.getTitle();
        this.user = hashtag.getUser();
    }
}
