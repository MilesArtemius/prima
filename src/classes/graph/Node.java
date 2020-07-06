package classes.graph;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Node {
    private Point2D position;
    private final String name;
    private LinkedList<Ark> arks = new LinkedList<Ark>();
    private boolean hidden = false;

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

    public Node(Point2D position, String name) {
        this.position = position;
        this.name = name;
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
}