package dev.Program.UI;

public class UI {

    private static final String RED_TEXT = "\u001B[31m";
    private static final String RESET = "\u001B[0m"; // Reset to default color


    public static void warning(String string) {
        System.out.println(RED_TEXT + string + RESET);
    }
 
    

}
