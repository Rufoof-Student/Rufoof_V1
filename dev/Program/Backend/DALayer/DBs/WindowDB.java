package dev.Program.Backend.DALayer.DBs;

import com.google.gson.annotations.SerializedName;

public class WindowDB {
    
    @SerializedName("AppName")
    private String appName;

    @SerializedName("Path")
    private String path;

    @SerializedName("Type")
    private String type; // e.g. word, excel, chrome

    @SerializedName("URL")
    private String url; // Optional field for browser tabs

    // Getters and Setters
    // ... (omitted for brevity)

    @Override
    public String toString() {
        return "Window{" +
                "appName='" + appName + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
