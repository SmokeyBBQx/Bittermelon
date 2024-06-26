package net.smokeybbq.bittermelon.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DataManager<U, T> {
    protected final Map<U, T> dataMap = new ConcurrentHashMap<>();
    protected final Gson gson = new Gson();
    private final String dataFolder;
    private final Class<T> type;

    protected DataManager(String dataFolder, Class<T> type) {
        this.dataFolder = dataFolder;
        this.type = type;
        new File(dataFolder).mkdirs();
        loadData();
    }

    public T getData(U key) {
        return dataMap.get(key);
    }

    public void addData(U key, T data) {
        saveData(data);
        dataMap.put(key, data);
    }

    public Map<U, T> getDataMap() {
        return dataMap;
    }

    protected void saveData(T data) {
        // Construct the folder path where the data will be stored, using the data's file name
        String folderName = dataFolder + "/" + getFileName(data);
        // Create the directory (and any necessary but nonexistent parent directories)
        new File(folderName).mkdirs();
        // Construct the full file path for the data's JSON file
        String fileName = folderName + "/" + getFileName(data) + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Serialize the data object to JSON and write it to the file
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadData() {
        // Finds the data folder -> finds all data directories -> checks for .json files inside directories
        File folder = new File(dataFolder);
        File[] listOfDirs = folder.listFiles();
        if (listOfDirs != null) {
            for (File dir : listOfDirs) {
                if (dir.isDirectory()) {
                    File[] listOfFiles = dir.listFiles();
                    if (listOfFiles != null) {
                        for (File file : listOfFiles) {
                            if (file.isFile() && file.getName().endsWith(".json")) {
                                try (FileReader reader = new FileReader(file)) {
                                    // Deserialize the JSON file into an object of type T
                                    T data = gson.fromJson(reader, type);
                                    // Add the deserialized object to the data map
                                    addData(getKey(data), data);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected abstract String getFileName(T data);
    protected abstract U getKey(T data);
}
