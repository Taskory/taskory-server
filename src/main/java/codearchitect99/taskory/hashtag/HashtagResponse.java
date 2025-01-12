package codearchitect99.taskory.hashtag;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HashtagResponse {
    private Long id;
    private String title;

    public HashtagResponse(Hashtag hashtag) {
        this.id = hashtag.getId();
        this.title = hashtag.getTitle();
    }
}
