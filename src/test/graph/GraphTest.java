package test.graph;

import classes.Log;
import classes.graph.Graph;

import java.awt.geom.Point2D;

public class GraphTest {
    public static void runTest() {
        boolean successful = true;

        Log.in().gui(true).attr(Log.Attributes.BOLD).say("Тест начался!");
        Graph graph = new Graph();

        Log.in().gui(true).say("Проверяем, пустой ли граф:");
        successful &= graph.isEmpty();
        Log.in().say(graph.isEmpty());
        if (!graph.isEmpty())
            Log.in().gui(true).attr(Log.Attributes.ITALIC).attr(Log.Attributes.BOLD).say("!Граф не пустой!");

        Log.in().gui(true).say("Создаём узел с именем 'Node':");
        graph.addNode(new Point2D.Double(0, 0), "Node");
        successful &= (graph.getNodes().size() == 1);
        if (!(graph.getNodes().size() == 1))
            Log.in().gui(true).attr(Log.Attributes.ITALIC).attr(Log.Attributes.BOLD).say("!Узел не создан!");

        Log.in().gui(true).say("Создаём узел с именем 'Node1':");
        graph.addNode(new Point2D.Double(15, 15), "Node1");
        successful &= (graph.getNodes().size() == 2);
        if (!(graph.getNodes().size() == 2))
            Log.in().gui(true).attr(Log.Attributes.ITALIC).attr(Log.Attributes.BOLD).say("!Узел не создан!");


        Log.in().gui(true).say("Создаём ребро с весом 7:");
        graph.addArk("Node","Node1", 7);
        successful &= (graph.getArks().size() == 1);
        if (!(graph.getArks().size() == 1))
            Log.in().gui(true).attr(Log.Attributes.ITALIC).attr(Log.Attributes.BOLD).say("!Ребро не создано!");


        Log.in().gui(true).say("Удаляем ребро с весом 7:");
        graph.deleteArk("Node","Node1");
        successful &= (graph.getArks().size() == 0);
        if (!(graph.getArks().size() == 0))
            Log.in().gui(true).attr(Log.Attributes.ITALIC).attr(Log.Attributes.BOLD).say("!Ребро не удалено!");

        Log.in().gui(true).say("Удаляем узел с именем 'Node1':");
        graph.deleteNode("Node1");
        successful &= (graph.getNodes().size() == 1);
        if (!(graph.getNodes().size() == 1))
            Log.in().gui(true).attr(Log.Attributes.ITALIC).attr(Log.Attributes.BOLD).say("!Узел не удален!");

        Log.in().gui(true).say("Удаляем узел с именем 'Node':");
        graph.deleteNode("Node");
        successful &= (graph.getNodes().size() == 0);
        if (!(graph.getNodes().size() == 0))
            Log.in().gui(true).attr(Log.Attributes.ITALIC).attr(Log.Attributes.BOLD).say("!Узел не удален!");

        Log.in().gui(true).attr(Log.Attributes.BOLD).end("!").say("Тестирование прошло ", successful ? "успешно" : "провально");
    }
}
