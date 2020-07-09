package classes.algorithm;

import classes.Log;
import classes.graph.Graph;
import classes.graph.Ark;
import classes.graph.Node;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PrimaAlgorithm implements Algorithm {

    /**
     * SwingWorker<Void, Void> worker = new SwingWorker<>() {
     *     @Override
     *     public Void doInBackground() throws Exception {
     *         // Perform the time-taking task, this method is executed in new thread.
     *         // NB! Log is thread-safe (afaik).
     *         return null;
     *     }
     *     @Override
     *         public void done() {
     *             try {
     *                 get(); // Get results and errors (if any).
     *             } catch (Exception e) {
     *                 e.printStackTrace();
     *             }
     *         }
     *     };
     *
     *     worker.execute();
     */

    private Graph graph;
    private ArrayList<Node> nodesForSearch = new ArrayList<Node>();
    private boolean isPrepared = false;
    private void logResult(){
        for (Ark ark: graph.getArks()){

            System.out.println("Ребро с весом " + Double.toString(ark.getWeight()) + (ark.isHidden()? " не добавлено.": " добавлено."));
        }


    }

    public int CountHidden(){//для тестов
        int count = 0;
        for (Ark ark: graph.getArks()){
            if (ark.isHidden()) {

            } else {
                count++;
            }
        }
        return count;
    }

    public Thread threadSolve(Graph graph){

        Thread thread = new Thread(() -> solveStep(graph));
        thread.start();
        return thread;
    }

    public Thread threadSolveAll(Graph graph){
        Thread thread = new Thread(() -> solve(graph));
        thread.start();
        return thread;

    }
    @Override
    public Graph solve(Graph graph){//сюда изначальный целый граф. Само запустит функции подготовки и решения.
        prepareGraph(graph);
        while (solveStep() != null) {
            //решаем
        }
        logResult();
        return graph;
    }

    @Override
    public Graph solveStep(Graph graph){//сюда только получающееся остовное дерево (граф с hidden ark). Каждый шаг добавляется одно ребро.
    //возвращает null если решение завершено
        if (graph.isRecentlyChanged() || !isPrepared){
            prepareGraph(graph);
            return graph;
        }
        if (!nodesForSearch.isEmpty()){
            double weight = Double.POSITIVE_INFINITY;
            Ark minArk = null;
            boolean isStartInArrMin = false;
            for (Ark ark: graph.getArks()){
                boolean isStartInArr = nodesForSearch.contains(graph.getNode(ark.getStart()));
                boolean isEndInArr = nodesForSearch.contains(graph.getNode(ark.getEnd()));
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
                nodesForSearch.add(isStartInArrMin? graph.getNode(minArk.getEnd()) : graph.getNode(minArk.getStart()));
                nodesForSearch.get(nodesForSearch.size()-1).showNode();
                Log.gui().say("Найдено ребро ", minArk);

            }
            else{
                //решение завершено
                Log.gui().say("Решение завершено");
                return null;
            }


        }

        return graph;
    }

    public Graph solveStep(){
        if (graph == null){

            Log.gui().say("Граф не найден");
            return null;
        }
        return solveStep(graph);
    }

    @Override
    public Graph prepareGraph(Graph graph) {//сюда изначальный целый граф. Превращает граф в граф без ребер и добавляет стартовое ребро.
        this.graph = graph;
        if (graph.isRecentlyChanged()){
            isPrepared = false;
            graph.setRecentlyChanged(false);
        }
        if (isPrepared){
            return null;
        }

        isPrepared = true;
        nodesForSearch = new ArrayList<Node>();
        for (Ark ark: graph.getArks()) {
            ark.hideArk();
        }
        for (Node node: graph.getNodes()){
            node.hideNode();
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, graph.getNodes().size());
        Log.gui().say("Начальный узел: ", graph.getNodes().get(randomNum).getName());
        nodesForSearch.add(graph.getNodes().get(randomNum));
        nodesForSearch.get(0).showNode();
        solveStep();
        return graph;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }
}
