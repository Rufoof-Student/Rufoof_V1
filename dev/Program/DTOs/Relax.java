package dev.Program.DTOs;

public class Relax {
    public static void Relax(long ms){
        try {
            Thread.currentThread().sleep(ms);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
