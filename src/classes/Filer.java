package classes;

import classes.graph.Graph;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Properties;

public class Filer {
    public static final String PROPERTIES_FILE_EXTENSION = "properties";
    public static final String GRAPH_FILE_EXTENSION = "sv";

    public static void printToFile(String string, String fileName, OnPerformed listener) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                Files.write(Paths.get(fileName), (string + "\n").getBytes(), Files.exists(Paths.get(fileName)) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
                return null;
            }
            @Override
            public void done() {
                try {
                    get();
                    listener.onFinished(null);
                } catch (Exception e) {
                    listener.onFinished(e);
                }
            }
        };

        worker.execute();
    }



    public static void saveGraphToFile(Graph g, String fileName, OnPerformed listener) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                innerSaveGraph(g, fileName);
                return null;
            }
            @Override
            public void done() {
                try {
                    get();
                    listener.onFinished(null);
                } catch (Exception e) {
                    listener.onFinished(e);
                }
            }
        };

        worker.execute();
    }

    public static void saveGraphToFileNoThread(Graph g, String fileName, OnPerformed listener) {
        try {
            innerSaveGraph(g, fileName);
            listener.onFinished(null);
        } catch (IOException e) {
            listener.onFinished(e);
        }
    }

    private static void innerSaveGraph(Graph g, String fileName) throws IOException {
        if (!fileName.endsWith("." + GRAPH_FILE_EXTENSION)) fileName += "." + GRAPH_FILE_EXTENSION;
        Files.deleteIfExists(Paths.get(fileName));
        Files.createFile(Paths.get(fileName));
        FileOutputStream outputStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(g.writeToMap());
        objectOutputStream.close();
    }

    public static void loadGraphFromFile(String fileName, boolean graphic, OnGraphLoaded listener) {
        SwingWorker<Graph, Void> worker = new SwingWorker<>() {
            @Override
            public Graph doInBackground() throws Exception {
                String name;
                if (!fileName.endsWith("." + GRAPH_FILE_EXTENSION)) name = fileName + "." + GRAPH_FILE_EXTENSION;
                else name = fileName;
                FileInputStream fileInputStream = new FileInputStream(name);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                return Graph.readFromMap((Map<String, Object>) objectInputStream.readObject(), graphic);
            }
            @Override
            public void done() {
                try {
                    Graph graph = get();
                    listener.onFinished(graph, null);
                } catch (Exception e) {
                    listener.onFinished(null, e);
                }
            }
        };

        worker.execute();
    }



    public static void savePropertiesToFile(Properties properties, String fileName, OnPerformed listener) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                FileOutputStream fos = new FileOutputStream(fileName);
                properties.store(new OutputStreamWriter(fos, StandardCharsets.UTF_16), null);
                fos.close();
                return null;
            }
            @Override
            public void done() {
                try {
                    get();
                    listener.onFinished(null);
                } catch (Exception e) {
                    listener.onFinished(e);
                }
            }
        };

        worker.execute();
    }

    public static void loadPropertiesFromFile(String fileName, OnPropertiesLoaded listener) {
        SwingWorker<Properties, Void> worker = new SwingWorker<>() {
            @Override
            public Properties doInBackground() throws Exception {
                Properties prop = new Properties();
                FileInputStream fist = new FileInputStream(fileName);
                prop.load(new InputStreamReader(fist, StandardCharsets.UTF_16));
                fist.close();
                return prop;
            }
            @Override
            public void done() {
                try {
                    Properties prop = get();
                    listener.onFinished(prop, null);
                } catch (Exception e) {
                    listener.onFinished(null, e);
                }
            }
        };

        worker.execute();
    }



    public static void copyFile(String fileName, String newFileName, OnPerformed listener) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                Files.copy(Paths.get(fileName), Paths.get(newFileName), StandardCopyOption.REPLACE_EXISTING);
                return null;
            }
            @Override
            public void done() {
                try {
                    get();
                    listener.onFinished(null);
                } catch (Exception e) {
                    listener.onFinished(e);
                }
            }
        };

        worker.execute();
    }

    public static void deleteFile(String fileName, OnPerformed listener) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                Files.deleteIfExists(Paths.get(fileName));
                return null;
            }
            @Override
            public void done() {
                try {
                    get();
                    listener.onFinished(null);
                } catch (Exception e) {
                    listener.onFinished(e);
                }
            }
        };

        worker.execute();
    }

    public static void removeFolder(String dirName, OnPerformed listener) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<>() {
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
                return null;
            }
            @Override
            public void done() {
                try {
                    get();
                    listener.onFinished(null);
                } catch (Exception e) {
                    listener.onFinished(e);
                }
            }
        };

        worker.execute();
    }

    public static void addFolder(String dirName, OnPerformed listener) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws Exception {
                Path path = Paths.get(dirName);
                if (!Files.exists(path) || !Files.isDirectory(path)) Files.createDirectory(Paths.get(dirName));
                return null;
            }
            @Override
            public void done() {
                try {
                    get();
                    listener.onFinished(null);
                } catch (Exception e) {
                    listener.onFinished(e);
                }
            }
        };

        worker.execute();
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
