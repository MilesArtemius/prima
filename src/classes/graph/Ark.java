package classes.graph;

public class Ark {

    private final Node start, end;
    private final double weight;
    private boolean hidden = false;

    public Ark(Node start, Node end, double weight){
        this.start = start;
        this.end = end;
        this.weight = weight;

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
/////////////////////////////////////
    //public void setStart(Node start, Node end) {
    //    this.start = start;
    //    this.end = end;
    // }


}