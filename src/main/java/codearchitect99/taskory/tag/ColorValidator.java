package codearchitect99.taskory.tag;

import codearchitect99.taskory.tag.model.Color;

public class ColorValidator {
    public static boolean isValidColor(String color) {
        if (color == null) {
            return false;
        }
        try {
            Color.valueOf(color.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
