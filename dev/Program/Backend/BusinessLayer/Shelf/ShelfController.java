package dev.Program.Backend.BusinessLayer.Shelf;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.Program.Backend.DALayer.MainDBControlers.ShelfControllerDB;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Exceptions.DeveloperException;

public class ShelfController {
    private Map<Integer, Shelf> shelfs;
    private int idCounter;

    public ShelfController(){
        shelfs = ShelfControllerDB.getAllShelfs();
        idCounter=getMaxId();
    }

    private int getMaxId() {
        int maxId = 0;
        for (Integer shelfId : shelfs.keySet()) {
            maxId = maxId>shelfId?maxId:shelfId;
        }    
        return maxId;
    }

    public Shelf getNewShelf(String name, Colors color) {
        return new Shelf(name, idCounter++ , color);
    }

    public void saveShelf(Shelf toSave) throws DeveloperException {
        if(shelfs.containsKey(toSave.getId())){
            throw new DeveloperException("trying to save the same key on shelf controller");
        }
        shelfs.put(toSave.getId(), toSave);
        ShelfControllerDB.saveShelf(toSave);
    }

    public List<Shelf> getAllShelfsAsList() {
        List<Shelf> toRet = new ArrayList<>();
        for (Shelf shelf : shelfs.values()) {
            toRet.add(shelf);
        }
        return toRet;
    }
}
