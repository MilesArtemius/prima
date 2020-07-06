package classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Settings {
    private HashMap<String, Double> constants;
    private ResourceBundle dictionary;
    private static Settings instance;

    private static String [] locales = new String [] {"", "De", "La", "Ru"};

    private void initializeDefaultConstants() {
        try {
            String fileName = "constants";
            ResourceBundle constantsBundle = ResourceBundle.getBundle(fileName, new UTF8Control());
            constants = new HashMap<>();
            for (String key: constantsBundle.keySet()) {
                constants.put(key, Double.parseDouble(constantsBundle.getString(key)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDictionary(Locale locale) {
        try {
            String fileName = "localization";
            dictionary = ResourceBundle.getBundle(fileName, locale, new UTF8Control());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private Settings() {
        initializeDictionary(Locale.forLanguageTag("Ru"));
        initializeDefaultConstants();
    }

    private static Settings get() {
        if (instance == null) instance = new Settings();
        return instance;
    }

    public static void setup() {
        get();
    }



    public static String getString(String key, Object... args) {
        return String.format(get().dictionary.getString(key), args);
    }

    public static int getInt(String key) {
        return (int) get().constants.get(key).doubleValue();
    }

    public static double getDouble(String key) {
        return get().constants.get(key);
    }



    public static void changeLocalization(int localePosition) {
        get().initializeDictionary(Locale.forLanguageTag(locales[localePosition]));
    }

    public static boolean changeParameter(String name, double value) {
        if (get().constants.containsKey(name)) {
            get().constants.put(name, value);
            return true;
        } else return false;
    }



    private static class UTF8Control extends ResourceBundle.Control {
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            // The below is a copy of the default implementation.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // Only this line is changed to make it to read properties files as UTF-8.
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_16));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
