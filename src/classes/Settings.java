package classes;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {
    private static final String constantsName = "constants";
    private static final String dictionaryName = "localization";

    public static final String userPath = "USER_PATH";
    public static final String userLocalization = "USING_USER_LOCALIZATION";
    public static final String preservedGraph = "PRESERVED_GRAPH";

    public static final String userPathDir = "PrimaConfigurationFolder";
    private static final String userPathConstants = File.separator + userPathDir + File.separator + constantsName + "." + Filer.PROPERTIES_FILE_EXTENSION;
    private static final String userPathDictionary = File.separator + userPathDir + File.separator + dictionaryName + "." + Filer.PROPERTIES_FILE_EXTENSION;

    private HashMap<String, Long> constants;
    private HashMap<String, String> dictionary;
    private static Log.Level logLevel;
    private static Settings instance;

    public enum Locales {
        USER("*Dummy string, will be replaced later*", "User"), ENGLISH("English", "En"),
        GERMAN("Deutsch", "De"), LATIN("Lingua latina", "La"), RUSSIAN("Русский язык", "");
        private String locale, symbol;

        Locales(String locale, String symbol) {
            this.locale = locale;
            this.symbol = symbol;
        }
        public String getLocale() {
            return locale;
        }
        public String getSymbol() {
            return symbol;
        }
    }

    private void initializeDefaultConstants() {
        Log.getForLevel(logLevel).info().say("Загрузка параметров по умолчанию...");
        boolean restoreLevel = logLevel == Log.Level.GUI;
        logLevel = Log.Level.FILE;

        try {
            Log.getForLevel(logLevel).say("Загрузка параметров из файла '", constantsName, "':");
            ResourceBundle constantsBundle = ResourceBundle.getBundle(constantsName, new UTF16Control());
            constants = new HashMap<>();
            for (String key: constantsBundle.keySet()) {
                Log.getForLevel(logLevel).beg("\t").say(key, " -> ", constantsBundle.getString(key));
                constants.put(key, Long.decode(constantsBundle.getString(key)));
            }
        } catch (Exception e) {
            Log.consumeException("Ошибка при загрузке параметров по умолчанию!", e);
        }

        if (restoreLevel) logLevel = Log.Level.GUI;
        Log.getForLevel(logLevel).info().say("Загрузка параметров по умолчанию успешно завершена!");

        if (checkPref(userPath)) {
            Log.getForLevel(logLevel).info().say("Загрузка пользовательских параметров...");
            restoreLevel = logLevel == Log.Level.GUI;

            Log.getForLevel(logLevel).say("Загрузка параметров из файла '", getPref(userPath) + userPathConstants, "':");
            Filer.loadPropertiesFromFile(getPref(userPath) + userPathConstants, (properties, reason) -> {
                boolean restoreLogLevel = logLevel == Log.Level.GUI;
                logLevel = Log.Level.FILE;

                if (reason == null) {
                    for (Object key : properties.keySet()) {
                        Log.getForLevel(logLevel).beg("\t").say(key, " -> ", properties.get(key.toString()).toString());
                        if (constants.containsKey(key.toString())) constants.put(key.toString(), Long.decode(properties.get(key.toString()).toString()));
                    }
                } else Log.getForLevel(logLevel).warn().say("Файл не найден или пользовательские настройки не заданы!");

                if (restoreLogLevel) logLevel = Log.Level.GUI;
                Log.getForLevel(logLevel).good().say("Загрузка пользовательских параметров успешно завершена!");
            });

            if (restoreLevel) logLevel = Log.Level.GUI;
        }
    }

    private void initializeDictionary(Locale locale) {
        Log.getForLevel(logLevel).info().say("Загрузка локализации по умолчанию...");
        boolean restoreLevel = logLevel == Log.Level.GUI;
        logLevel = Log.Level.FILE;

        try {
            Log.getForLevel(logLevel).say("Загрузка локализации из файла '", dictionaryName, "':");
            ResourceBundle dictionaryBundle = ResourceBundle.getBundle(dictionaryName, locale, new UTF16Control());
            dictionary = new HashMap<>();
            for (String key: dictionaryBundle.keySet()) {
                Log.getForLevel(logLevel).beg("\t").say(key, " -> ", dictionaryBundle.getString(key));
                dictionary.put(key, dictionaryBundle.getString(key));
            }
        } catch (Exception e) {
            Log.consumeException("Ошибка при загрузке локализации по умолчанию!", e);
        }

        if (restoreLevel) logLevel = Log.Level.GUI;
        Log.getForLevel(logLevel).good().say("Загрузка локализации по умолчанию успешно завершена!");
    }

    private void initializeUserDictionary(OnLongActionFinished listener) {
        initializeDictionary(Locale.forLanguageTag(""));
        if (checkPref(userPath) && checkPref(userLocalization)) {
            Log.getForLevel(logLevel).info().say("Загрузка пользовательской локализации по умолчанию...");
            boolean restoreLevel = logLevel == Log.Level.GUI;
            logLevel = Log.Level.FILE;

            Log.getForLevel(logLevel).say("Загрузка локализации из файла '", getPref(userPath) + userPathDictionary, "':");
            Filer.loadPropertiesFromFile(getPref(userPath) + userPathDictionary, (properties, reason) -> {
                boolean restoreLogLevel = logLevel == Log.Level.GUI;
                logLevel = Log.Level.FILE;

                if (reason == null) for (Object key : properties.keySet()) {
                    Log.cui().beg("\t").say(key, " -> ", properties.get(key).toString());
                    if (dictionary.containsKey(key.toString())) dictionary.put(key.toString(), properties.get(key).toString());
                } else Log.getForLevel(logLevel).warn().say("Файл не найден или пользовательская локализация не задана!");

                if (restoreLogLevel) logLevel = Log.Level.GUI;
                Log.getForLevel(logLevel).good().say("Загрузка пользовательской локализации успешно завершена!");
                if (listener != null) listener.onFinished();
            });

            if (restoreLevel) logLevel = Log.Level.GUI;
        } else if (listener != null) listener.onFinished();
    }



    private Settings() {
        logLevel = Log.Level.GUI;
        Log.getForLevel(logLevel).say("Initializing settings...");
        initializeUserDictionary(null);
        initializeDefaultConstants();
    }

    private static Settings get() {
        if (instance == null) instance = new Settings();
        return instance;
    }

    public static void setup(Log.Level level) {
        logLevel = level;
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

    public static boolean getBoolean(String key) {
        return getInt(key) != 0;
    }

    public static HashMap<String, String> getConstantsDescription() {
        HashMap<String, String> constantsDescription = new HashMap<>(get().constants.size());
        for (String key: get().dictionary.keySet()) if (get().constants.containsKey(key)) constantsDescription.put(key, get().dictionary.get(key));
        return constantsDescription;
    }



    public static void changeLocalization(Locales locale, OnLongActionFinished listener) {
        Log.gui().info().say("Замена локализации...");
        if (locale == Locales.USER) {
            Log.gui().info().say("Установка пользовательской локализации...");
            setPref(userLocalization, userLocalization);
            get().initializeUserDictionary(() -> {
                Log.gui().good().say("Установка пользовательской локализации успешно завершена!");
                if (listener != null) listener.onFinished();
            });
        } else {
            Log.gui().info().say("Установка локализации для языка " + locale.name() + "...");
            resetPref(userLocalization);
            get().initializeDictionary(Locale.forLanguageTag(locale.getSymbol()));
            Log.gui().good().say("Установка локализации для языка " + locale.name() + " успешно завершена!");
            if (listener != null) listener.onFinished();
        }
    }



    public static void alterUserPath(String path, OnLongActionFinished listener) {
        Log.gui().info().say("Установка пути к файлам конфигурации...");
        setPref(userPath, path);
        Filer.addFolder(path + File.separator + userPathDir, reason -> {
            if (reason != null) Log.consumeException("Ошибка при создании файлов конфигурации!", reason);
            else {
                Log.gui().good().say("Установка пути к файлам конфигурации успешно завершена!");
                Log.gui().info().say("Сохранение текущих настроек в файл конфигурации...");
                Properties prop = new Properties();
                for (Map.Entry<String, Long> entry: get().constants.entrySet()) prop.setProperty(entry.getKey(), entry.getValue().toString());
                Filer.savePropertiesToFile(prop, getPref(userPath) + userPathConstants, reason1 -> {
                    if (reason1 != null) Log.consumeException("Ошибка при сохранении текущих настроек в файл конфигурации!", reason1);
                    else Log.gui().good().say("Сохранение текущих настроек в файл конфигурации успешно завершено!");
                    if (listener != null) listener.onFinished();
                });
            }
            if (listener != null) listener.onFinished();
        });
    }

    public static void removeUserPath(OnLongActionFinished listener) {
        Log.gui().info().say("Удаление файлов конфигурации...");
        if (checkPref(userPath)) Filer.removeFolder(getPref(userPath) + File.separator + userPathDir, reason -> {
            if (reason != null) reason.printStackTrace();
            else {
                clearPrefs();
                get().initializeDefaultConstants();
                get().initializeUserDictionary(null);
            }
            if (listener != null) listener.onFinished();
        });
        else if (listener != null) listener.onFinished();
    }

    public static void resetConstants(OnLongActionFinished listener) {
        if (checkPref(userPath)) Filer.deleteFile(getPref(userPath) + userPathConstants, reason -> {
            get().initializeDefaultConstants();
            if (listener != null) listener.onFinished();
        });
        else if (listener != null) listener.onFinished();
    }

    public static void resetDictionary(OnLongActionFinished listener) {
        if (checkPref(userPath)) Filer.deleteFile(getPref(userPath) + userPathDictionary, reason -> {
            resetPref(userLocalization);
            get().initializeUserDictionary(null);
            if (listener != null) listener.onFinished();
        });
        else if (listener != null) listener.onFinished();
    }



    public static boolean checkPref(String key) {
        Preferences prefs = Preferences.userNodeForPackage(Prima.class);
        return !prefs.get(key, "").equals("");
    }

    public static String getPref(String key) {
        Preferences prefs = Preferences.userNodeForPackage(Prima.class);
        return prefs.get(key, "");
    }

    public static void setPref(String key, String value) {
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            prefs.put(key, value);
            prefs.flush();
            prefs.sync();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public static void resetPref(String key) {
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            prefs.remove(key);
            prefs.flush();
            prefs.sync();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public static void clearPrefs() {
        try {
            Preferences prefs = Preferences.userNodeForPackage(Prima.class);
            prefs.clear();
            prefs.removeNode();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }



    public static void alterLocalization(String file, OnLongActionFinished listener) {
        if (checkPref(userPath)) Filer.copyFile(file, getPref(userPath) + userPathDictionary, reason -> {
            if (reason != null) reason.printStackTrace();
            else changeLocalization(Locales.USER, () -> {
                if (listener != null) listener.onFinished();
            });
        });
        else if (listener != null) listener.onFinished();
    }

    public static void alterParameter(String name, long value, OnLongActionFinished listener) {
        if (get().constants.containsKey(name)) {
            get().constants.put(name, value);

            if (checkPref(userPath)) {
                Filer.loadPropertiesFromFile(getPref(userPath) + userPathConstants, (properties, reason) -> {
                    Properties prop = new Properties();
                    if (reason == null) prop = properties;
                    prop.setProperty(name, String.valueOf(value));
                    Filer.savePropertiesToFile(prop, getPref(userPath) + userPathConstants, reason1 -> {
                        if (reason1 != null) reason1.printStackTrace();
                        if (listener != null) listener.onFinished();
                    });
                });
            } else {
                Log.cui().say("Warning! Path for parameter storing not found, parameter changed until session end!");
                if (listener != null) listener.onFinished();
            }
        } else{
            Log.cui().say("Can not alter - no such parameter found!");
            if (listener != null) listener.onFinished();
        }
    }



    public interface OnLongActionFinished {
        void onFinished();
    }



    private static class UTF16Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            // The below is a copy of the default implementation.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, Filer.PROPERTIES_FILE_EXTENSION);
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
            } else stream = loader.getResourceAsStream(resourceName);
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
