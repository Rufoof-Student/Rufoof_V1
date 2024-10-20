package dev.Program.Backend.DALayer.DBs;
import com.google.gson.annotations.SerializedName;

import dev.Program.Backend.BusinessLayer.Shelf.Shelf;

import java.util.*;


public class ShelfDB {
    @SerializedName("ShelfId")
    public int shelfId; // Unique id for each shelf

    @SerializedName("ShelfName")
    public String shelfName; // User-defined name for the shelf

    @SerializedName("ShelfColor")
    public String shelfColor; // Representing color as a String

    @SerializedName("Note")
    public String note;

    @SerializedName("isOpen")
    public boolean isOpen;

    @SerializedName("Windows")
    public List<MicrosoftAppDB> windows; // List of Window objects

    @SerializedName("ChromeGroups")
    public List<GroupDB> chromeGroups;

    @SerializedName("EdgeGroups")
    public List<GroupDB> edgeGroups;
    // Getters and Setters
    // ... (omitted for brevity)

    @Override
    public String toString() {
        return "ShelfDB{" +
                "shelfId='" + shelfId + '\'' +
                ", shelfName='" + shelfName + '\'' +
                ", shelfColor='" + shelfColor + '\'' +
                ", note='" + note + '\'' +
                ", isOpen=" + isOpen +
                ", windows=" + windows +
                '}';
    }

    public static ShelfDB SerializedShelf(Shelf s) {
        ShelfDB result = new ShelfDB();
        result.shelfId = s.getId(); // Assuming Shelf has a method getId()
        result.isOpen = s.getIsOpen(); // Assuming Shelf has a method getIsOpen()
        result.shelfName = s.getName(); // Assuming Shelf has a method getName()
        result.shelfColor = s.getColor(); // Assuming Shelf has a method getColor()
        result.note = s.getNote(); // Assuming Shelf has a method getNote()
        result.windows = new ArrayList<>();
        result.chromeGroups = new ArrayList<>();
        result.edgeGroups = new ArrayList<>();
        // Convert List<Window> from Shelf to List<WindowDB>
        // List<WindowDB> windowDBList = new ArrayList<>();
        // for (Window w : s.getWindows()) { // Assuming Shelf has a method getWindows()
        //     WindowDB windowDB = new WindowDB();
        //     windowDB.setAppName(w.getAppName()); // Assuming Window has a method getAppName()
        //     windowDB.setPath(w.getPath()); // Assuming Window has a method getPath()
        //     windowDB.setType(w.getType()); // Assuming Window has a method getType()
        //     windowDB.setUrl(w.getUrl()); // Assuming Window has a method getUrl()
        //     windowDBList.add(windowDB);
        // }
        // result.windows = windowDBList;
    
        return result;
    }
}