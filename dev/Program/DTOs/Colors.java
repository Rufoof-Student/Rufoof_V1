package dev.Program.DTOs;

import java.util.*;

public enum Colors {
    GREY("grey", 128, 128, 128),
    BLUE("blue", 0, 0, 255),
    RED("red", 255, 0, 0),
    YELLOW("yellow", 255, 255, 0),
    GREEN("green", 0, 128, 0),
    PINK("pink", 255, 192, 203),
    PURPLE("purple", 128, 0, 128),
    CYAN("cyan", 0, 255, 255),
    ORANGE("orange", 255, 165, 0);

    private final String colorName;
    private final int r;
    private final int g;
    private final int b;

    // Constructor with name and RGB
    Colors(String colorName, int r, int g, int b) {
        this.colorName = colorName;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    // Constructor with RGB only (defaults to a generic name like "Custom Color")
    Colors(int r, int g, int b) {
        this.colorName = "Custom Color";  // Default name if not provided
        this.r = r;
        this.g = g;
        this.b = b;
    }

    // Constructor with just name (defaults to black as a fallback RGB value)
    Colors(String colorName) {
        this.colorName = colorName;
        this.r = 0;  // Default RGB value if not provided
        this.g = 0;
        this.b = 0;
    }

    // Getter for color name
    public String getColorName() {
        return colorName;
    }

    // Getters for RGB values
    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public static List<Colors> getAllColors(){
        List<Colors> toRet  = new ArrayList<>();
        for (Colors color : Colors.values()) {
            toRet.add(color);
            System.out.println(color.colorName);
        }
        return toRet;
    }

    @Override
    public String toString() {
        return colorName ;
    }
}
