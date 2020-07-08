package classes.graph;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Graph {


    private HashMap<String, Node> nodes = new HashMap<String, Node>();
    private LinkedList<Ark> arks = new LinkedList<Ark>(); // я бы предложил создавать массив в момент вызова getArks(), чтобы избежать хранения лишнего, хотя так быстрее

    public Node getNode(String name){

        return nodes.get(name);
    }

    public void addNode(Point2D position, String name){

        nodes.put(name, new Node(position, name));
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

    public void changeNode(String originName, String newName){

        Node node = nodes.get(originName);
        if (node != null){
            nodes.remove(originName);
            nodes.put(newName, node);
            node.setName(newName);
        }
    }

    public void addArk(Node start, Node end, int weight){
        if (start.getArkTo(end) == null){//обратной тоже не будет, у нас тут неориентированный граф
            Ark ark = new Ark(start, end, weight);
            arks.add(ark);
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
            arks.remove(arkToDelete);
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

    public ArrayList<Node> getNodes() {
        return new ArrayList<Node>(nodes.values());

        /* старая часть для тестов
        ArrayList<Node> nodesArr = new ArrayList<Node>(nodes.values());
        ArrayList<Node> nodesArr = new ArrayList<>();
        nodesArr.add(new Node(new Point2D.Double(1, 2), "1"));
        nodesArr.add(new Node(new Point2D.Double(10, 20), "2"));
        nodesArr.add(new Node(new Point2D.Double(100, 200), "3"));
        nodesArr.add(new Node(new Point2D.Double(278, 347), "4"));
        return nodesArr;

         */
    }

    public List<Ark> getArks() {
        return arks;

        /* старая часть для тестов
        LinkedList<Ark> arks = new LinkedList<>();
        arks.add(new Ark(getNodes().get(0), getNodes().get(1), 10.0));
        arks.add(new Ark(getNodes().get(2), getNodes().get(3), 27.5));

         */
    }

    private void setPosition(int total, int number) {//для чего? этот метод позволяет расставить начальные позиции для узлов, если они не заданы. если у нас создание узлов только по щелчку, можешь удалять.
        double ang = 360.0 / total * number;
        double trueAng = Math.toRadians(90) - Math.toRadians(ang);
        int x = 401 + (int) (Math.cos(trueAng) * 200);
        int y = 301 - (int) (Math.sin(trueAng) * 200);
    }


}