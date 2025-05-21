package ru.netology.graphics.image;

public class ColorSchema implements TextColorSchema {
    private final char[] chars = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        int index = color * (chars.length - 1) / 255;
        return chars[index];
    }
}