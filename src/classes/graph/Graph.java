package classes.graph;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

public class Graph {


    private HashMap<String, Node> nodes = new HashMap<String, Node>();

    public Node getNode(String name){

        return nodes.get(name);
    }

    public void addNode(String name){

        nodes.put(name, new Node(name));
    }

    public void deleteNode(String name){

        Node node = nodes.get(name);
        if (node != null){

            for (int i = node.getArks().size()-1; i>=0; i--) {
                deleteArk(node, node.getArks().get(i).getStart() == node ? node.getArks().get(i).getEnd() : node.getArks().get(i).getStart());
            }
            nodes.remove(name);
        }
    }

    public void addArk(Node start, Node end, int weight){
        if (start.getArkTo(end) == null){//обратной тоже не будет, у нас тут неориентированный граф
            Ark ark = new Ark(start, end, weight);
            start.getArks().add(ark);
            end.getArks().add(ark);
        }
        else {

            //уже есть путь
        }

    }
    public void addArk(String strStart, String strEnd, int weight){
        Node start = nodes.get(strStart);
        Node end = nodes.get(strEnd);
        if (start!=null && end!=null){

            addArk(start, end, weight);
        }

    }


    public void deleteArk(Node start, Node end){
        Ark arkToDelete = start.getArkTo(end);
        if (arkToDelete != null){
            start.getArks().remove(arkToDelete);
            end.getArks().remove(arkToDelete);
        }

    }

    public void deleteArk(String strStart, String strEnd){
        Node start = nodes.get(strStart);
        Node end = nodes.get(strEnd);
        if (start!=null && end!=null){

            deleteArk(start, end);
        }

    }

    public boolean isEmpty(){

        return nodes.isEmpty();
    }






///////////////////////
//////////////////////
/*
    public List<Node> getNodes() {
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(new Node(new Point2D.Double(1, 2), "1"));
        nodes.add(new Node(new Point2D.Double(10, 20), "2"));
        nodes.add(new Node(new Point2D.Double(100, 200), "3"));
        nodes.add(new Node(new Point2D.Double(278, 347), "4"));
        return nodes;
    }

    public List<Ark> getArks() {
        LinkedList<Ark> arks = new LinkedList<>();
        arks.add(new Ark(getNodes().get(0), getNodes().get(1), 10.0));
        arks.add(new Ark(getNodes().get(2), getNodes().get(3), 27.5));
        return arks;
    }

    private void setPosition(int total, int number) {
        double ang = 360.0 / total * number;
        double trueAng = Math.toRadians(90) - Math.toRadians(ang);
        int x = 401 + (int) (Math.cos(trueAng) * 200);
        int y = 301 - (int) (Math.sin(trueAng) * 200);
    }


 */
}