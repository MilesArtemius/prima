package classes.graph;

public class Ark {
    private Node start, end;
    private boolean isTargeted;

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

    public boolean isTargeted() {
        return isTargeted;
    }

    public void setTargeted(boolean targeted) {
        isTargeted = targeted;
    }
}
