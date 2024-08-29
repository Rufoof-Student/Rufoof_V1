package dev.Program.Backend.BusinessLayer.Shelf;

import java.util.LinkedHashMap;
import java.util.Map;

import dev.Program.DTOs.Colors;

public class ShelfController {
    private Map<Integer, Shelf> shelfs;
    private int idCounter;

    public ShelfController(){
        idCounter=0;
        shelfs = new LinkedHashMap<>();
    }

    public Shelf getNewShelf(String name, Colors color) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNewShelf'");
    }
}
