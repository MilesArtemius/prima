package test.algorithm;

import classes.Log;
import classes.Settings;
import classes.algorithm.PrimaAlgorithm;
import classes.graph.Graph;
import classes.shapes.GraphShape;

import javax.swing.*;
import java.awt.*;

import static classes.Prima.prepareInput;

public class PrimaAlgorithmTest {
    public static void runTest() {
        boolean success = true;

        Log.gui(Log.Attributes.BOLD).say("Тест алгоритма начался!");



        Graph graph = prepareInput();
        GraphShape shape = new GraphShape();
        shape.setGraph(graph);

        PrimaAlgorithm alg = new PrimaAlgorithm();
        alg.solve(graph);

        Log.gui().say("Проверяем правильность решения");
        success &= (alg.CountHidden() == (graph.getNodes().size()-1));
        if (!(alg.CountHidden() == (graph.getNodes().size()-1)))
            Log.gui(Log.Attributes.ITALIC,Log.Attributes.BOLD ).say("!Решение не верно!");

        Log.gui(Log.Attributes.BOLD).end("!").say("Тестирование прошло ", success ? "успешно" : "провально");
    }
}
