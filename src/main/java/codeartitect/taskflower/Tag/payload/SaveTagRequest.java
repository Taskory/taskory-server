package codeartitect.taskflower.Tag.payload;

import codeartitect.taskflower.Tag.model.Color;
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
