package Service;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class History {
    private final Collection<Object[]> history;

    public History() {
        this.history = new ArrayList<>();
        if (historyFileAlreadyExist()) {
            for (Object[] arr : getHistory()) history.add(arr);
        }
    }

    public Object[][] getHistory() {
        if (historyFileAlreadyExist()) {
            try (FileReader reader = new FileReader("history.json")) {
                return getGson().fromJson(reader, Object[][].class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new Object[][]{};
    }

    public void saveFile(Object[] arr) {
        history.add(arr);
        try (FileWriter writer = new FileWriter("history.json")) {
            writer.write(getGson().toJson(history));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean historyFileAlreadyExist() {
        return new File("history.json").exists();
    }

    private Gson getGson() {
        return new Gson();
    }
}
