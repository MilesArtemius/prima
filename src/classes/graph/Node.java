package classes.graph;

import java.io.*;
import java.util.LinkedList;

public class Node implements Externalizable {

    private static final long serialVersionUID = 3L;
    private String name;
    private LinkedList<Ark> arks = new LinkedList<Ark>();
    private boolean hidden;

    public String getName() {
        return name;
    }

    public LinkedList<Ark> getArks() {
        return arks;
    }

    public Ark getArkTo(Node node){

        if (!arks.isEmpty()){

            for (Ark ark:arks) {
                if (ark.getStart() == node || ark.getEnd() == node){//неважно на каком конце нода которую ищем

                    return ark;
                }
            }
        }

        return null;
    }

    public void setName(String name){

        this.name = name;
    }

    public Node(String name) {
        this.name = name;
        this.hidden = true;
    }

    public void hideNode(){

        this.hidden = true;
    }

    public void showNode(){

        this.hidden = false;
    }

    public boolean isHidden(){

        return hidden;
    }

    @Override
    public String toString(){

        return "Узел "+ name;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        // default serialization
        oos.defaultWriteObject();
        // write the object
        System.out.println("New w action node!");
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        // default deserialization
        ois.defaultReadObject();
        System.out.println("New r action node!");

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}