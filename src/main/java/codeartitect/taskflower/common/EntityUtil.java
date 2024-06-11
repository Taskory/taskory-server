package codeartitect.taskflower.common;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {
    public static <T> List<T> setListElements(List<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return new ArrayList<>();
        } else {
            return elements;
        }
    }
}
