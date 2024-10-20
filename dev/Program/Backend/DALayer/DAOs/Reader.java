package dev.Program.Backend.DALayer.DAOs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.text.html.parser.Parser;

import com.google.gson.Gson;

import dev.Program.Backend.DALayer.FileDetector;
import dev.Program.Backend.DALayer.DBs.ShelfDB;
import dev.Program.DTOs.Exceptions.DeveloperException;

import java.util.List;
import java.util.ArrayList;

public class Reader extends Protocol {

    public  List<ShelfDB> getAllShelfs() {
        List<ShelfDB> allShelfsResult = new ArrayList<>(); 
        File[] allFiles = dataDirectory.listFiles();
        for(File shelfFile : allFiles){
            try {
                String shelfAsJson = Files.readString(shelfFile.toPath());
                ShelfDB shelf = gson.fromJson(shelfAsJson, ShelfDB.class);
                allShelfsResult.add(shelf);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return allShelfsResult;
    }

    @Override
    public  void ifFileExistBehavior(String fileName) {
        if(!fileIsExited(fileName)){
            throw new DeveloperException(fileName+ " is not exist!");
        }
    }

    public  ShelfDB getShelfObjFromDB(int id){
        File shelfFile = getFileForShlef(id);
        try {
            String shelfAsJson = Files.readString(shelfFile.toPath());
            ShelfDB shelf = gson.fromJson(shelfAsJson, ShelfDB.class);
            return (shelf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new DeveloperException("shelf not found in data "+ id);
        }
        
    }

}
