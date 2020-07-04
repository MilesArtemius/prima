package classes.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node {
    //private Point2D position;
    private final String name;
    private ArrayList<Ark> arks;


    public Node(String name){
        this.name = name;

    }
    public String getName() {
        return name;
    }

    public ArrayList<Ark> getArks() {
        return arks;
    }

    public Ark getArkTo(Node node){

        for (Ark ark:arks) {
            if (ark.getStart() == node || ark.getEnd() == node){//неважно на каком конце нода которую ищем

                return ark;
            }
        }
        return null;
    }

    //public Node(Point2D position, String name) {
    //    this.position = position;
    //    this.name = name;
    //}



    //public Point2D getPosition() {
    //    return position;
    //}

    // public void setPosition(Point2D position) {
    //   this.position = position;
    // }
}