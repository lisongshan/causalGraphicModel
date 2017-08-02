package net.ipsoft.ipcenter.iprca.causality.learning.gies.operation;

import net.ipsoft.ipcenter.iprca.causality.graph.algorithm.graphTypeChecking.EssentialGraphChecker;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.InterventionFamily;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.ScoreFunction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sli on 11/20/15.
 */
public abstract class Operation {
    protected InterventionFamily interventionFamily;
    protected ChainGraph essentialGraph;
    protected Node u;
    protected Node v;
    protected ScoreFunction scoreFunction;

    protected double maxScoreChange;
    protected List<Node> cliqueArgMaxC;

    protected ChainGraph essentialGraph_old;

    public Operation(InterventionFamily interventionFamily, ChainGraph essentialGraph, Node u, Node v, ScoreFunction scoreFunction) throws Exception {
        this.interventionFamily = interventionFamily;
        this.essentialGraph = essentialGraph;
        this.u = u;
        this.v = v;
        this.scoreFunction = scoreFunction;

        //TODO remove the following debug code:
        essentialGraph_old = new ChainGraph(essentialGraph.getNodes(), essentialGraph.getEdges());

        EssentialGraphChecker checker = new EssentialGraphChecker(essentialGraph, interventionFamily);

            if(!checker.isTrue()){
                System.out.println(" this is wrong");
                throw new Exception("EssentialGraphChecker failed in Operation constructor");
            }

    }


//    public Node getV() {
//        return v;
//    }
//
//    public Node getU() {
//        return u;
//    }

    public double getMaxScoreChange() {
        return maxScoreChange;
    }

    public void setCliqueArgMaxC(List<Node> cliqueArgMaxC) {
        this.cliqueArgMaxC = cliqueArgMaxC;
    }

    public List<Node> getCliqueArgMaxC() {
        return cliqueArgMaxC;
    }

    protected static <T> List<T> union(List<T>... setLists) {
        Set<T> union = new HashSet<>();
        for(List<T> set :setLists ){
            union.addAll(set);
        }
        return union.stream().collect(Collectors.toList());
    }




    public static <T> List<T> intersect(List<T>... setLists) {

        if(setLists.length == 0){
            return Arrays.asList();
        }
        Set<T> intersection = new HashSet<>();
        intersection.addAll(setLists[0]);
        for(int i=1; i<setLists.length; i++){
            intersection.retainAll(setLists[i]);
        }

        return intersection.stream().collect(Collectors.toList());
    }


    public  static <T> List<T> diff(List<T> parentU0, List<T> nodes) {
        Set<T> difference = new HashSet<>();
        difference.addAll(parentU0);
        difference.removeAll(nodes);

        return difference.stream().collect(Collectors.toList());
    }

    public abstract Operation propose() throws Exception;
    public abstract void commit();

    protected void validate() throws Exception {

        //TODO remove the following debug code:
        essentialGraph_old = new ChainGraph(essentialGraph.getNodes(), essentialGraph.getEdges());

        EssentialGraphChecker checker = new EssentialGraphChecker(essentialGraph, interventionFamily);

        if(!checker.isTrue()){
            System.out.println(" this is wrong");
            throw new Exception("EssentialGraphChecker failed in Operation constructor");
        }


    }
}
