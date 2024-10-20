package dev.Program.Backend.DALayer.DAOs;

import com.google.errorprone.annotations.ForOverride;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.Program.Backend.DALayer.DBs.ShelfDB;
import dev.Program.DTOs.Exceptions.DeveloperException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Protocol {
    protected static String dataDirPath = "C:\\shelf";
    protected static File dataDirectory = new File(dataDirPath);

    protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();


    
    protected static boolean fileIsExited(String fileName) {
        Path path = Paths.get(dataDirectory + "\\" + fileName);
        return Files.exists(path);
    }

    protected static String getFileName(int id){
        return "shelf_"+id+".json";
    }

    @ForOverride
    public  void ifFileExistBehavior(String fileName){
        throw new DeveloperException("if file is existed must be implemented !");
    }

    protected  File getFileForShlef(int id) {
        String fileName = getFileName(id);
        ifFileExistBehavior(fileName);
        File shelfFile = new File(dataDirPath + "\\" + fileName);
        
        return shelfFile;
    }

    

   

}
