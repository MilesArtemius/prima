package classes;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {
    private static final String constantsName = "constants";
    private static final String dictionaryName = "localization";

    private static final String userPath = "USER_PATH";

    private static final String userPathConstants = File.pathSeparator + constantsName + ".properties";
    private static final String userPathDictionary = File.pathSeparator + dictionaryName + ".properties";

    private HashMap<String, Long> constants;
    private HashMap<String, String> dictionary;
    private static Settings instance;

    private static String [] locales = new String [] {"User", "", "De", "La", "Ru"};

    private void initializeDefaultConstants() {
        try {
            Log.in().say("Loading constants from file ", constantsName, ":");
            ResourceBundle constantsBundle = ResourceBundle.getBundle(constantsName, new UTF16Control());
            constants = new HashMap<>();
            for (String key: constantsBundle.keySet()) {
                Log.in().beg("\t").say(key, " -> ", constantsBundle.getString(key));
                constants.put(key, Long.decode(constantsBundle.getString(key)));
            }

            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            String pathToUserConstants = prefs.get(userPath, "");
            if (!pathToUserConstants.equals("")) {
                Log.in().say("Loading user defined constants:");
                ClassLoader loader = new URLClassLoader(new URL[] {new File(pathToUserConstants + userPathConstants).toURI().toURL()});
                constantsBundle = ResourceBundle.getBundle(constantsName, Locale.getDefault(), loader, new UTF16Control());
                for (String key: constantsBundle.keySet()) {
                    Log.in().beg("\t").say(key, " -> ", constantsBundle.getString(key));
                    if (constants.containsKey(key)) constants.put(key, Long.decode(constantsBundle.getString(key)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDictionary(Locale locale) {
        try {
            Log.in().say("Loading localization from file ", dictionaryName, ":");
            ResourceBundle dictionaryBundle = ResourceBundle.getBundle(dictionaryName, locale, new UTF16Control());
            dictionary = new HashMap<>();
            for (String key: dictionaryBundle.keySet()) {
                Log.in().beg("\t").say(key, " -> ", dictionaryBundle.getString(key));
                dictionary.put(key, dictionaryBundle.getString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUserDictionary() {
        initializeDictionary(Locale.forLanguageTag("Ru"));
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            String pathToUserDictionary = prefs.get(userPath, "");
            if (!pathToUserDictionary.equals("")) {
                Log.in().say("Loading user defined localization:");
                ClassLoader loader = new URLClassLoader(new URL[] {new File(pathToUserDictionary + userPathDictionary).toURI().toURL()});
                ResourceBundle dictionaryBundle = ResourceBundle.getBundle(dictionaryName, Locale.getDefault(), loader, new UTF16Control());
                for (String key: dictionaryBundle.keySet()) {
                    Log.in().beg("\t").say(key, " -> ", dictionaryBundle.getString(key));
                    if (dictionary.containsKey(key)) dictionary.put(key, dictionaryBundle.getString(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private Settings() {
        Log.in().say("Initializing settings...");
        initializeUserDictionary();
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
        return String.format(get().dictionary.get(key), args);
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
        if (localePosition == 0) get().initializeUserDictionary();
        else get().initializeDictionary(Locale.forLanguageTag(locales[localePosition]));
    }



    public static void alterUserPath(String path) {
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            prefs.put(userPath, path);
            prefs.sync();
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public static void removeUserPath() {
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            prefs.clear();
            prefs.removeNode();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUserPathSet() {
        Preferences prefs = Preferences.userNodeForPackage(Prima.class);
        return prefs.get(userPath, "").equals("");
    }



    public static boolean alterLocalization(String name) {
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            String pathToUserDictionary = prefs.get(userPath, "");
            if (!pathToUserDictionary.equals("")) {
                Files.copy(Path.of(name), Path.of(pathToUserDictionary + userPathDictionary));
            }
            changeLocalization(0);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean alterParameter(String name, long value) {
        if (get().constants.containsKey(name)) {
            get().constants.put(name, value);

            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            String pathToUserConstants = prefs.get(userPath, "");
            if (!pathToUserConstants.equals("")) {
                try {
                    Properties prop = new Properties();
                    prop.load(new InputStreamReader(get().getClass().getResourceAsStream(pathToUserConstants + userPathConstants), StandardCharsets.UTF_16));
                    prop.setProperty(name, String.valueOf(value));
                    prop.store(new FileOutputStream(pathToUserConstants + userPathConstants), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;
        } else return false;
    }



    private static class UTF16Control extends ResourceBundle.Control {
        @Override
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
