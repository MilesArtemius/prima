package classes.graph;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.LinkedList;

public class Node implements Serializable {

    private static final long serialVersionUID = 3L;
    private Point2D position;
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

    public Node(Point2D position, String name) {
        this.position = position;
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

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    @Override
    public String toString(){

        return "Узел "+ name;
    }
}