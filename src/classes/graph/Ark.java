package classes.graph;

import java.io.Serializable;

public class Ark implements Serializable {

    private static final long serialVersionUID = 2L;
    private final Node start, end;
    private final double weight;
    private boolean hidden;

    public Ark(Node start, Node end, double weight){
        this.start = start;
        this.end = end;
        this.weight = weight;
        this.hidden = true;

    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public double getWeight(){

        return weight;
    }

    public void hideArk(){

        this.hidden = true;
    }

    public void showArk(){

        this.hidden = false;
    }

    public boolean isHidden(){

        return hidden;
    }

    @Override
    public String toString(){

        return "Ребро "+ start.getName() + "<->" + end.getName() + ", вес: " + Double.toString(weight);
    }


}