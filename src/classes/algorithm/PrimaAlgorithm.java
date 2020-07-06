package classes.algorithm;

import classes.graph.Graph;
import classes.graph.Ark;
import classes.graph.Node;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PrimaAlgorithm implements Algorithm {

    private Graph startGraph;
    private Graph currentGraph;
    private ArrayList<Node> nodesForSearch = new ArrayList<Node>();
    private ArrayList<Ark> addedArks = new ArrayList<Ark>();

    @Override
    public Graph solve(Graph graph){//сюда изначальный целый граф. Само запустит функции подготовки и решения.

        Graph nextGraph = prepareGraph(graph);
        while (solveStep() != null) {
            //решаем
        }
        return graph;
    }

    @Override
    public Graph solveStep(Graph graph){//сюда только получающееся остовное дерево (граф с hidden ark). Каждый шаг добавляется одно ребро.
    //возвращает null если решение завершено

        if (!nodesForSearch.isEmpty()){
            double weight = Double.POSITIVE_INFINITY;
            Ark minArk = null;
            boolean isStartInArrMin = false;
            for (Ark ark: graph.getArks()){
                boolean isStartInArr = nodesForSearch.contains(ark.getStart());
                boolean isEndInArr = nodesForSearch.contains(ark.getEnd());
                if (ark.isHidden() && (isStartInArr ^ isEndInArr)){
                    if (weight > ark.getWeight()){
                        minArk = ark;
                        weight = minArk.getWeight();
                        isStartInArrMin = isStartInArr;
                    }

                }
            }
            if (minArk != null){

                minArk.showArk();
                nodesForSearch.add(isStartInArrMin? minArk.getEnd() : minArk.getStart());

            }
            else
                return null;

        }

        return graph;
    }

    public Graph solveStep(){

        return solveStep(graph);
    }

    @Override
    public Graph prepareGraph(Graph graph) {//сюда изначальный целый граф. Превращает граф в граф без ребер и добавляет стартовое ребро.
        this.graph = graph;
        for (Ark ark: graph.getArks()) {
            ark.hideArk();
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, graph.getNodes().size());
        nodesForSearch.add(graph.getNodes().get(randomNum));
        solveStep();
        return graph;
    }
}
