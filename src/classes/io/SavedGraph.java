package classes.io;

import java.io.*;

import classes.graph.Graph;
import classes.graph.Ark;
import classes.graph.Node;

import java.util.HashMap;
import java.util.LinkedList;

public class SavedGraph {

    private Graph graph;
    private static final String DEFAULT_NAME = "save.sv";


    public SavedGraph(Graph graph){
        this.graph = graph;

    }
    public void save() {
        save(DEFAULT_NAME, "");
    }
    public void save(String saveName, String path) {

        try{
            FileOutputStream outputStream = new FileOutputStream(path+saveName+".sv");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(graph);

            objectOutputStream.close();

        }
        catch (IOException e){
            System.out.println("Файл не найден или содержимое файла повреждено.");
        }


    }
    public Graph load() {
        return load(DEFAULT_NAME, "");
    }
    public Graph load(String saveName, String path) {

        try{
            FileInputStream fileInputStream = new FileInputStream(path + saveName+".sv");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);


            graph = (Graph) objectInputStream.readObject();

       }
       catch (IOException | ClassNotFoundException e){
           System.out.println("Файл не найден или содержимое файла повреждено.");
       }


        //System.out.println(graph);
        return graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }
}
