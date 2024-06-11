package codeartitect.taskflower.tag.payload;

import codeartitect.taskflower.tag.model.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveTagRequest {
    private String tile;

    private Color color;
}
