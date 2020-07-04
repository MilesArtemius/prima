package classes.graph;

public class Ark {
    private Node start, end;
    double weight;

    public void setStart(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public double getWeight() {
        return weight;
    }

    public Ark(Node start, Node end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }
}
