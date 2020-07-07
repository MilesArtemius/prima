package test.graph;

import classes.Log;
import classes.graph.Graph;

import java.awt.geom.Point2D;

public class GraphTest {
    public static void runTest() {
        boolean successful = true;

        Log.in().gui(true).say("Тест начался!");
        Graph graph = new Graph();

        Log.in().gui(true).say("Проверяем, пустой ли граф:");
        successful &= graph.isEmpty();
        Log.in().say(graph.isEmpty());

        Log.in().gui(true).say("Создаём узел с именем 'Node':");
        graph.addNode(new Point2D.Double(0, 0), "Node");
        successful &= (graph.getNodes().size() == 1);

        Log.in().gui(true).end("!").say("Тестирование прошло ", successful ? "успешно" : "провально");
    }
}
