package dev.Program.Backend.DALayer.DBs;
import com.google.gson.annotations.SerializedName;
import java.util.List;

class ShelfDB {
    @SerializedName("ShelfId")
    private String shelfId; // Unique id for each shelf

    @SerializedName("ShelfName")
    private String shelfName; // User-defined name for the shelf

    @SerializedName("ShelfColor")
    private String shelfColor; // Representing color as a String

    @SerializedName("Note")
    private String note;

    @SerializedName("isOpen")
    private boolean isOpen;

    @SerializedName("Windows")
    private List<WindowDB> windows; // List of Window objects

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
}