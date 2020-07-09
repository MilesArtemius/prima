package classes.graph;

import java.io.*;
import java.util.*;

public class Graph implements Externalizable {

    private HashMap<String, Node> nodes = new HashMap<String, Node>();
    private LinkedList<Ark> arks = new LinkedList<Ark>(); // я бы предложил создавать массив в момент вызова getArks(), чтобы избежать хранения лишнего, хотя так быстрее


    private boolean recentlyChanged = true;

    public Node getNode(String name){

        return nodes.get(name);
    }

    public void addNode(Node node){

        nodes.put(node.getName(), node);
        setRecentlyChanged(true);
    }

    public void deleteNode(String name){

        Node node = nodes.get(name);
        if (node != null){

            for (int i = node.getArks().size()-1; i>=0; i--) {
                deleteArk(node, getNode(node.getArks().get(i).getStart()) == node ? getNode(node.getArks().get(i).getEnd()) : getNode(node.getArks().get(i).getStart()));
            }
            nodes.remove(name);
            setRecentlyChanged(true);
        }
    }

    public void changeNode(String originName, String newName){

        Node node = nodes.get(originName);
        if (node != null){
            nodes.remove(originName);
            nodes.put(newName, node);
            node.setName(newName);
            setRecentlyChanged(true);
        }
    }

    public void addArk(Node start, Node end, int weight){
        if (start.getArkTo(end) == null){//обратной тоже не будет, у нас тут неориентированный граф
            Ark ark = new Ark(start.getName(), end.getName(), weight);
            arks.add(ark);
            start.getArks().add(ark);
            end.getArks().add(ark);
            setRecentlyChanged(true);
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
            setRecentlyChanged(true);
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


    public boolean isRecentlyChanged() {
        return recentlyChanged;
    }

    public void setRecentlyChanged(boolean recentlyChanged) {
        if (recentlyChanged){

            for (Ark ark: getArks()) {
                ark.hideArk();
            }
            for (Node node: getNodes()){
                node.hideNode();
            }
        }
        this.recentlyChanged = recentlyChanged;
    }



    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        LinkedList<Map<String, Object>> nodeList = new LinkedList<>();
        for (Map.Entry<String, Node> entry: nodes.entrySet()) {
            nodeList.push(entry.getValue().writeToMap());
        }
        out.writeObject(nodeList);
        out.writeObject(arks);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        LinkedList<Map<String, Object>> nodeList = (LinkedList<Map<String, Object>>) in.readObject();
        nodes = new HashMap<>();
        for (Map<String, Object> map: nodeList) {
            Node node;
            if (map.containsKey("POSITION")) node = NodePlus.readFromMap(map);
            else node = Node.readFromMap(map);
            nodes.put(node.getName(), node);
        }
        arks = (LinkedList<Ark>) in.readObject();
    }
}