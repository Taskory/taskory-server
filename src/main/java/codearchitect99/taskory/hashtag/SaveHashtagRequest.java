package codearchitect99.taskory.hashtag;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveHashtagRequest {
    @NotNull(message = "Title cannot be null.")
    private String title;
}
