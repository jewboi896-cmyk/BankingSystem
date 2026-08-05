package com.bank.file;

import com.bank.factory.GsonFactory;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class FileHelper<T> {
    private final Gson gson;
    private final String filePath;
    private final Type type;

    public FileHelper(String filePath, Type type) {
        this.gson = GsonFactory.createGson();
        this.filePath = filePath;
        this.type = type;
    }

    /**
     * @author Derek Homel
     * @summary
     * @return returns a Map<UUID, Type> so that it doesnt matter the type of
     * the file being loaded
     */
    public @NotNull Map<UUID, T> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (FileReader reader = new FileReader(file)) {
            Map<UUID, T> loaded = gson.fromJson(reader, type);
            return loaded != null ? loaded : new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from " + filePath, e);
        }
    }

    /**
     * @author Derek Homel
     * @summary
     * @param data the data that needs to be written to the file
     */
    public void writeToFile(Map<UUID, T> data) {
        File file = new File(filePath);
        File parent = file.getParentFile(); // create data/ directory if missing
        if (parent != null) { parent.mkdirs(); }
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, type, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write data to " + filePath, e);
        }
    }
}
