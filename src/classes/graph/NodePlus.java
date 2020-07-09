package classes.graph;

import java.awt.geom.Point2D;
import java.io.*;

public class NodePlus extends Node {

    private static final long serialVersionUID = 4L;
    private Point2D position;

    public NodePlus(Point2D position, String name) {
        super(name);
        this.position = position;
    }
    public NodePlus(String name) {
        super(name);
        this.position = new Point2D.Double(-1, -1);
    }

    public NodePlus() {}

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }


    /*protected void writeObject(ObjectOutputStream oos) throws IOException {
        // default serialization
        oos.defaultWriteObject();
        // write the object
        System.out.println("New w action!");
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        // default deserialization
        ois.defaultReadObject();
        System.out.println("New r action!");

    }*/

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(position);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        position = (Point2D) in.readObject();
    }
}
