package dev.Program.Backend.BusinessLayer.Shelf;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.Program.DTOs.Colors;

public class ShelfController {
    private Map<Integer, Shelf> shelfs;
    private int idCounter;

    public ShelfController(){
        idCounter=-1;
        shelfs = new LinkedHashMap<>();
    }

    public Shelf getNewShelf(String name, Colors color) {
        return new Shelf(name, idCounter++ , color);
    }

    public void saveShelf(Shelf toSave) {
        if(shelfs.containsKey(toSave.getId())){
            throw new IllegalArgumentException("trying to save the same key on shelf controller");
        }
        shelfs.put(toSave.getId(), toSave);
    }

    public List<Shelf> getAllShelfsAsList() {
        List<Shelf> toRet = new ArrayList<>();
        for (Shelf shelf : shelfs.values()) {
            toRet.add(shelf);
        }
        return toRet;
    }
}
