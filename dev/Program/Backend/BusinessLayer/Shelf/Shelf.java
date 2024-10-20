package dev.Program.Backend.BusinessLayer.Shelf;

import java.util.List;
import java.util.ArrayList;

import dev.Program.Backend.DALayer.DBs.ShelfDB;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.GroupPack;
import dev.Program.DTOs.Window;

public class Shelf {
    private String name;
    private int id;
    private Colors color;
    private String note;
    private boolean isOpen;

    public Shelf(String name,int id,Colors color){
        this.id = id;
        this.name = name;
        this.color= color;
    }

    public Shelf(ShelfDB shelfDB) {
        name= shelfDB.shelfName;
        id = shelfDB.shelfId;
        color = Colors.valueOfColor(shelfDB.shelfColor);
        note = shelfDB.note;
        isOpen = shelfDB.isOpen;
    }

    public String toString(){
        return name+" with id "+id+" and color "+color.getColorName();
    }
    
    public int getId() {
        return id;
    }
    public String getName() {
       return name;
    }
    public String getColor() {
        return color.getColorName();
    }
    public Colors getColorAsEnum(){
        return color;
    }



    public void markAsClosed() {
        // groups.markAsClosed();
        isOpen=false;
    }


    public boolean getIsOpen() {
        return isOpen;    
    }

    public String getNote() {
        return note;
    }

    public void setName(String shelfName) {
        name=shelfName;
    }



}
