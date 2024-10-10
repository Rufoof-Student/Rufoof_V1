package dev;

import java.io.Console;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dev.Program.UI.BasicUI;

public class Main{
    public static void main(String[] args){
        System.out.println("Hello World!");
        EventQueue.invokeLater(() -> {
            BasicUI mainUI = new BasicUI();
            mainUI.setVisible(true);
        });
    }
}