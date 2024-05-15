package net.smokeybbq.bittermelon.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
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
        String fileName = dataFolder + "/" + getFileName(data) + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadData() {
        File folder = new File(dataFolder);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    try (FileReader reader = new FileReader(file)) {
                        T data = gson.fromJson(reader, type);
                        addData(getKey(data), data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected abstract U getFileName(T data);
    protected abstract U getKey(T data);
}
