package dev.Program.Backend.BusinessLayer.Shelf;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import dev.Program.Backend.DALayer.DAOs.Writer;
import dev.Program.Backend.DALayer.DBs.ShelfDB;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Exceptions.DeveloperException;
import dev.Program.DTOs.Exceptions.UserException;

public class ShelfController {
    private Map<Integer, Shelf> shelfs;
    private int idCounter=0;
    private Writer Writer=new Writer();
    
    public ShelfController(List<ShelfDB> allShelfsFromDB) {
        shelfs = new HashMap<>();
        for (ShelfDB shelfDB : allShelfsFromDB) {
            Shelf shelfToAdd = new Shelf(shelfDB); 
            shelfs.put(shelfToAdd.getId(),shelfToAdd);
            idCounter = idCounter<shelfToAdd.getId()?shelfToAdd.getId():idCounter;
        }
        idCounter++;
    }

 
    public Shelf getNewShelf(String name, Colors color) {
        return new Shelf(name, idCounter++ , color);
    }

    public void saveShelf(Shelf toSave) throws DeveloperException {
        if(shelfs.containsKey(toSave.getId())){
            throw new DeveloperException("trying to save the same key on shelf controller");
        }
        shelfs.put(toSave.getId(), toSave);
        Writer.persistShelf(toSave);
    }

    public List<Shelf> getAllShelfsAsList() {
        List<Shelf> toRet = new ArrayList<>();
        for (Shelf shelf : shelfs.values()) {
            toRet.add(shelf);
        }
        return toRet;
    }

    public void updateShelfName(int id, String shelfName) {
        if(shelfName==null || shelfName=="") throw new UserException("Cant set empty shelf name");
        if(!shelfs.containsKey(id)) throw new DeveloperException("shelf id is not exist "+id);
        shelfs.get(id).setName(shelfName);
    }
}
