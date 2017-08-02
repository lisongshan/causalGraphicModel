package net.ipsoft.ipcenter.iprca.causality.scoreFunction;

import net.ipsoft.ipcenter.iprca.causality.graph.algorithm.graphTypeChecking.DagChecker;
import net.ipsoft.ipcenter.iprca.causality.graph.algorithm.transformer.DagGenerator;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;
import net.ipsoft.ipcenter.iprca.causality.interventionData.NodeProb;

import java.util.List;

/**
 * Created by sli on 11/10/15.
 */
public abstract class  ScoreFunction {
    protected DataSet dataSet;

    public ScoreFunction(){

    }

    public ScoreFunction(DataSet dataSet) {
        this.dataSet = dataSet;
    }


    public abstract double getLocalScore(Node node, List<Node> parentNodes) throws Exception;

    public double score(ChainGraph graph) throws Exception {

        ChainGraph subject = graph.deepCopy();
        DagChecker checker = new DagChecker(subject);
        boolean validate =  checker.isTrue();
        if(!validate){
            //assuming it is an essential graph, so generate a dag from it
            DagGenerator dg = new DagGenerator(subject);
            dg.toDag();
        }

        double score = 0;
        for(Node node : subject.getNodes()){
            score += this.getLocalScore(node, subject.getVertexByNumber(node.getNumber()).getParents());
        }
        return score;

    }

    public NodeProb getEmpiricalProb(Node node, NodesConfiguration parentConfig){

        DataSet dataSetInvAccounted = dataSet.accountforInvention(dataSet.getColumnNode(node.getNumber()));

        double[] probs = new double[node.getLevels().size()];
        double[] counts = new double[node.getLevels().size()];

        int totalCount = 0;
        for (int k = 0; k < node.getLevels().size(); k++) {
            NodesConfiguration combineConfig = new NodesConfiguration(parentConfig, node, node.getLevels().get(k));
            counts[k] = dataSetInvAccounted.getCount(combineConfig);
            totalCount += counts[k];
        }

        for (int k = 0; k < node.getLevels().size(); k++) {
            probs[k] = counts[k]*1.0/totalCount;
        }
        return new NodeProb(node, probs);
    }
}
