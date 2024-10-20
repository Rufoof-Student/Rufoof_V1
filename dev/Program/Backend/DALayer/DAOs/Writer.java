package dev.Program.Backend.DALayer.DAOs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroApp;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.Backend.DALayer.DBs.*;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.GroupPack;
import dev.Program.DTOs.Exceptions.DeveloperException;

public class Writer extends Protocol {

    private Reader Reader = new Reader();

    public  boolean persistShelf(Shelf shelf) {
        ShelfDB shelfDb = ShelfDB.SerializedShelf(shelf);
        return writeShelfDB(shelfDb);
    }

    private  boolean writeShelfDB(ShelfDB shelfDb) {
        File filetoSaveIn = getFileForShlef(shelfDb.shelfId);
        String shelfAsJSON = gson.toJson(shelfDb);
        try {
            Files.write(filetoSaveIn.toPath(), shelfAsJSON.getBytes());
            System.out.println("shelf file must be created!");
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    private static void createFile(String fileName) {
        Path path = Paths.get(dataDirectory + "\\" + fileName);
        try {
            Files.createFile(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public  void ifFileExistBehavior(String fileName){
        if (!fileIsExited(fileName))
            createFile(fileName);

    }

    public static void main(String[] args) {
        Shelf s = new Shelf("hi", 1, Colors.BLUE);
        // Writer.persistShelf(s);
    }


    public  void updateShelfMicroApps(int shelfId, List<MicroApp> apps) {
        List<MicrosoftAppDB> appsAsDOs = new ArrayList<>();
        for (MicroApp microApp : apps) {
            MicrosoftAppDB appAsDO = MicrosoftAppDB.Serialize(microApp);
            appsAsDOs.add(appAsDO);
        }
        ShelfDB shelfAsDO = Reader.getShelfObjFromDB(shelfId);
        shelfAsDO.windows = appsAsDOs;
        updateShelfInDB(shelfAsDO);
    }

    public  void updateShelfGroupsForChrome(int shelfId, GroupPack groupPack) {
        if(groupPack!=null){
            List<GroupDB> groupsAsDOs = GroupDB.serialize(groupPack);
            ShelfDB shelfAsDO = Reader.getShelfObjFromDB(shelfId);
            shelfAsDO.chromeGroups = groupsAsDOs;
            updateShelfInDB(shelfAsDO);
        }
    }

    public  void updateShelfGroupsForEdge(int shelfId, GroupPack groupPack) {
        if(groupPack!=null){
            List<GroupDB> groupsAsDOs = GroupDB.serialize(groupPack);
            ShelfDB shelfAsDO = Reader.getShelfObjFromDB(shelfId);
            shelfAsDO.edgeGroups = groupsAsDOs;
            updateShelfInDB(shelfAsDO);
        }
    }

    

    private  void updateShelfInDB(ShelfDB shelfAsDO) {
        

        writeShelfDB(shelfAsDO);
    }

 
    
}
