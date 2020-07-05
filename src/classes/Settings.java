package classes;

import java.util.Locale;
import java.util.Map;

public class Settings {
    // Graphical:
    String GRAPH_SHAPE_MULTIPLIER = "graph_shape_multiplier";



    // Language:



    private Map<String, Double> constants;
    private Map<String, String> dictionary;
    private static Settings instance;

    private void initializeDefaultConstants() {

    }

    private void initializeDictionary(Locale locale) {

    }



    private Settings() {

    }

    private static Settings get() {
        if (instance == null) instance = new Settings();
        return instance;
    }



    public static String getString(String key, double... args) {
        return get().dictionary.get(key);
    }

    public static double getInt(String key) {
        return (int) get().constants.get(key).doubleValue();
    }

    public static double getDouble(String key) {
        return get().constants.get(key);
    }
}
