package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import dev.Program.DTOs.Exceptions.UserException;

public class WordApp extends MicroApp {

    @Override
    public void checkFile(String path) throws UserException {
        
    }

    public WordApp(){
        appName = "Word.Application";
        fileFormat = ".docx";
    }


    public static void main(String[] args){
        WordApp word = new WordApp();
        try {
            word.setProps("C:\\Users\\bhaah\\Downloads\\Summary.docx");
        } catch (UserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        word.openApp();
    }
    
}
