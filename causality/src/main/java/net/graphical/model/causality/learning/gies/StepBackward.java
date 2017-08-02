package net.graphical.model.causality.learning.gies;

import net.graphical.model.causality.graph.model.intervention.InterventionFamily;
import net.graphical.model.causality.learning.gies.operation.Deletion;
import net.graphical.model.causality.scoreFunction.ScoreFunction;
import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.learning.gies.operation.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sli on 10/28/15.
 *
 * Algorithm 3
 */
public class StepBackward extends Step {
    public StepBackward(ChainGraph essentialGraph, ScoreFunction scoreFunction, InterventionFamily interventionFamily) {
        super(essentialGraph, scoreFunction, interventionFamily);
    }


    public void perturbEssentialGraph() throws Exception {

        Operation optimalOperation = null;

        for(Node v : essentialGraph.getNodes()){
            for(Node u : getNeigborOrParent(v) ){
                Operation deletion = new Deletion(interventionFamily, essentialGraph, u, v, scoreFunction).propose();
                if(optimalOperation == null || deletion.getMaxScoreChange() > optimalOperation.getMaxScoreChange()){
                    optimalOperation = deletion;
                }

            }
        }

        if(optimalOperation != null && optimalOperation.getMaxScoreChange() > SCORE_DIFF_THRESHOLD){
            optimalOperation.commit();
        }

    }

    private List<Node> getNeigborOrParent(Node node){
        Vertex v = essentialGraph.getVertexByNumber(node.getNumber());
        List<Node> np = new ArrayList<>();
        np.addAll(v.getNeighbors());
        np.addAll(v.getParents());
        return np;
    }
}
