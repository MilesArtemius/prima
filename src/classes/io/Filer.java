package classes.io;

import classes.Log;
import classes.graph.Graph;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

public class Filer {

    public static void printToFile(String string, String fileName, OnPerformed listener) {

    }



    public static void saveGraphToFile(Graph g, String fileName, OnPerformed listener) {
        try{
            FileOutputStream outputStream = new FileOutputStream(fileName+".sv");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(g);

            objectOutputStream.close();
            listener.onFinished(null);
        }
        catch (IOException e){
            Log.gui().say("Файл не найден или содержимое файла повреждено.");
            System.out.println("Файл не найден или содержимое файла повреждено.");
            listener.onFinished(e);
        }
    }

    public static void loadGraphFromFile(String fileName, OnGraphLoaded listener) {


        try{
            FileInputStream fileInputStream = new FileInputStream(fileName+".sv");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);


            Graph graph = (Graph) objectInputStream.readObject();
            listener.onFinished(graph, null);
        }
        catch (IOException | ClassNotFoundException e){
            Log.gui().say("Файл не найден или содержимое файла повреждено.");
            System.out.println("Файл не найден или содержимое файла повреждено.");
            listener.onFinished(null, e);
        }

    }



    public static void savePropertiesToFile(Properties properties, String fileName, OnPerformed listener) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            properties.store(new OutputStreamWriter(fos, StandardCharsets.UTF_16), null);
            fos.close();
            listener.onFinished(null);
        } catch (IOException e) {
            listener.onFinished(e);
        }
    }

    public static void loadPropertiesFromFile(String fileName, OnPropertiesLoaded listener) {
        try {
            Properties prop = new Properties();
            FileInputStream fist = new FileInputStream(fileName);
            prop.load(new InputStreamReader(fist, StandardCharsets.UTF_16));
            fist.close();
            listener.onFinished(prop, null);
        } catch (IOException e) {
            listener.onFinished(null, e);
        }
    }



    public static boolean fileExists(String fileName) {
        return Files.exists(Paths.get(fileName));
    }

    public static void copyFile(String fileName, String newFileName, OnPerformed listener) {
        try {
            Files.copy(Paths.get(fileName), Paths.get(newFileName), StandardCopyOption.REPLACE_EXISTING);
            listener.onFinished(null);
        } catch (IOException e) {
            listener.onFinished(e);
        }
    }

    public static void deleteFile(String fileName, OnPerformed listener) {
        try {
            Files.deleteIfExists(Paths.get(fileName));
            listener.onFinished(null);
        } catch (IOException e) {
            listener.onFinished(e);
        }
    }

    public static void removeFolder(String dirName, OnPerformed listener) {
        try {
            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>() {
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
            listener.onFinished(null);
        } catch (IOException e) {
            listener.onFinished(e);
        }
    }

    public static void addFolder(String dirName, OnPerformed listener) {
        try {
            Path path = Paths.get(dirName);
            if (!Files.exists(path) || !Files.isDirectory(path)) Files.createDirectory(Paths.get(dirName));
            listener.onFinished(null);
        } catch (IOException e) {
            listener.onFinished(e);
        }
    }



    public interface OnPerformed {
        void onFinished(Exception reason);
    }

    public interface OnGraphLoaded {
        void onFinished(Graph graph, Exception reason);
    }

    public interface OnPropertiesLoaded {
        void onFinished(Properties properties, Exception reason);
    }
}
