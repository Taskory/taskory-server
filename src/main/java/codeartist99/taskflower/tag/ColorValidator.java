package codeartist99.taskflower.tag;

import codeartist99.taskflower.tag.model.Color;

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
