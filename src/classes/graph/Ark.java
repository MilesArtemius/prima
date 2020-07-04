package classes.graph;

public class Ark {

    private final Node start, end;
    private final int weight;
    //private boolean isTargeted;

    public Ark(Node start, Node end, int weight){
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

    public int getWeight(){

        return weight;
    }
/////////////////////////////////////
    //public void setStart(Node start, Node end) {
    //    this.start = start;
    //    this.end = end;
    // }


//что это
    //public boolean isTargeted() {
    //    return isTargeted;
    //}

    //public void setTargeted(boolean targeted) {
    //    isTargeted = targeted;
    // }
}