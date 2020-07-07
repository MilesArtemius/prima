package classes;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.prefs.Preferences;

public class Settings {
    private static final String constantsName = "constants";
    private static final String dictionaryName = "localization";

    private static final String userPath = "USER_PATH";

    private static final String userPathDir = "PrimaConfigurationFolder";
    private static final String userPathConstants = File.separator + userPathDir + File.separator + constantsName + ".properties";
    private static final String userPathDictionary = File.separator + userPathDir + File.separator + dictionaryName + ".properties";

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
                Properties prop = new Properties();
                if (Files.exists(Paths.get(pathToUserConstants + userPathConstants)))
                    prop.load(new InputStreamReader(new FileInputStream(pathToUserConstants + userPathConstants), StandardCharsets.UTF_16));
                else Log.in().say("File exists, but empty!");
                for (Object key: prop.keySet()) {
                    Log.in().beg("\t").say(key, " -> ", prop.get(key.toString()).toString());
                    if (constants.containsKey(key.toString())) constants.put(key.toString(), Long.decode(prop.get(key.toString()).toString()));
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
                Properties prop = new Properties();
                if (Files.exists(Paths.get(pathToUserDictionary + userPathDictionary)))
                    prop.load(new InputStreamReader(new FileInputStream(pathToUserDictionary + userPathDictionary), StandardCharsets.UTF_16));
                else Log.in().say("File exists, but empty!");
                for (Object key: prop.keySet()) {
                    Log.in().beg("\t").say(key, " -> ", prop.get(key).toString());
                    if (dictionary.containsKey(key.toString())) dictionary.put(key.toString(), prop.get(key).toString());
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



    public static void changeLocalization(int localePosition) { // TODO: thread?
        if (localePosition == 0) get().initializeUserDictionary();
        else get().initializeDictionary(Locale.forLanguageTag(locales[localePosition]));
    }



    public static void alterUserPath(String path) {
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            prefs.put(userPath, path);
            prefs.sync();
            prefs.flush();

            Files.createDirectory(Paths.get(path + File.separator + userPathDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeUserPath() { // FIXME!!
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            String path = prefs.get(userPath, "");
            if (!path.equals("")) Files.walkFileTree(Paths.get(path + File.separator + userPathDir), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });

            prefs.clear();
            prefs.removeNode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isUserPathSet() {
        Preferences prefs = Preferences.userNodeForPackage(Prima.class);
        return !prefs.get(userPath, "").equals("");
    }



    public static void alterLocalization(String name) { // TODO: thread?
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            String pathToUserDictionary = prefs.get(userPath, "");
            if (!pathToUserDictionary.equals("")) {
                Files.copy(Paths.get(name), Paths.get(pathToUserDictionary + userPathDictionary));
            }
            changeLocalization(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void alterParameter(String name, long value) { // TODO: thread?
        if (get().constants.containsKey(name)) {
            get().constants.put(name, value);

            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            String pathToUserConstants = prefs.get(userPath, "");
            if (!pathToUserConstants.equals("")) {
                try {
                    Properties prop = new Properties();
                    if (Files.exists(Paths.get(pathToUserConstants + userPathConstants)))
                        prop.load(new InputStreamReader(new FileInputStream(pathToUserConstants + userPathConstants), StandardCharsets.UTF_16));
                    prop.setProperty(name, String.valueOf(value));
                    prop.store(new OutputStreamWriter(new FileOutputStream(pathToUserConstants + userPathConstants), StandardCharsets.UTF_16), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.in().say("Can not alter - no such parameter found!");
        }
    }

    public static HashMap<String, String> getConstantsDescription() {
        HashMap<String, String> constantsDescription = new HashMap<>(get().constants.size());
        for (String key: get().dictionary.keySet()) if (get().constants.containsKey(key)) constantsDescription.put(key, get().dictionary.get(key));
        return constantsDescription;
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
