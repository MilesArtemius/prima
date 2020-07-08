package test.graph;

import classes.Log;
import classes.graph.Graph;

import java.awt.geom.Point2D;

public class GraphTest {
    public static void runTest() {
        boolean successful = true;

        Log.gui(Log.Attributes.BOLD).say("Тест начался!");
        Graph graph = new Graph();

        Log.gui().say("Проверяем, пустой ли граф:");
        successful &= graph.isEmpty();
        Log.gui().say(graph.isEmpty());

        Log.gui().say("Создаём узел с именем 'Node':");
        graph.addNode(new Point2D.Double(0, 0), "Node");
        successful &= (graph.getNodes().size() == 1);

        Log.gui(Log.Attributes.BOLD).end("!").say("Тестирование прошло ", successful ? "успешно" : "провально");
    }
}
