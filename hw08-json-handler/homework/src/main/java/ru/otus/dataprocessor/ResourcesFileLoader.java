package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (var fileInputStream = new FileInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile());
             var bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            var gson = new Gson();
            var itemsListType = new TypeToken<List<Measurement>>() {}.getType();
            return gson.fromJson(bufferedReader, itemsListType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
