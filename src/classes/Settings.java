package classes;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Settings {
    private HashMap<String, Long> constants;
    private ResourceBundle dictionary;
    private static Settings instance;

    private static String [] locales = new String [] {"", "De", "La", "Ru"};

    private void initializeDefaultConstants() {
        try {
            String fileName = "constants";
            Log.in().say("Loading constants from file ", fileName, ":");
            ResourceBundle constantsBundle = ResourceBundle.getBundle(fileName, new UTF16Control());
            constants = new HashMap<>();
            for (String key: constantsBundle.keySet()) {
                Log.in().beg("\t").say(key, " -> ", constantsBundle.getString(key));
                constants.put(key, Long.decode(constantsBundle.getString(key)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDictionary(Locale locale) {
        try {
            String fileName = "localization";
            Log.in().say("Loading localization from file ", fileName, ":");
            dictionary = ResourceBundle.getBundle(fileName, locale, new UTF16Control());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private Settings() {
        Log.in().say("Initializing settings...");
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

    public static long getLong(String key) {
        return get().constants.get(key);
    }

    public static int getInt(String key) {
        return (int) get().constants.get(key).longValue();
    }

    public static Color getColor(String key) {
        long code = getLong(key);
        int r = (int) ((code & 0xff0000) >> 16);
        int g = (int) ((code & 0x00ff00) >> 8);
        int b = (int) (code & 0x0000ff);
        return new Color(r, g, b);
    }



    public static void changeLocalization(int localePosition) {
        get().initializeDictionary(Locale.forLanguageTag(locales[localePosition]));
    }

    public static boolean changeParameter(String name, long value) {
        if (get().constants.containsKey(name)) {
            get().constants.put(name, value);
            return true;
        } else return false;
    }



    private static class UTF16Control extends ResourceBundle.Control {
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
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
