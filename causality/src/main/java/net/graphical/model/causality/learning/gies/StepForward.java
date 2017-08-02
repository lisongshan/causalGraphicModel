package net.graphical.model.causality.learning.gies;

import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.intervention.InterventionFamily;
import net.graphical.model.causality.learning.gies.operation.Insertion;
import net.graphical.model.causality.learning.gies.operation.Operation;
import net.graphical.model.causality.scoreFunction.ScoreFunction;
import net.graphical.model.causality.graph.model.Node;

/**
 * Created by sli on 10/28/15.
 *
 * Algorithm 3
 */
public class StepForward extends Step {

    public StepForward(ChainGraph essentialGraph, ScoreFunction scoreFunction, InterventionFamily interventionFamily) {
        super(essentialGraph, scoreFunction, interventionFamily);
    }


    protected void perturbEssentialGraph() throws Exception {
        Operation optimalOperation = null;

        for(Node v : essentialGraph.getNodes()){
            for(Node u : essentialGraph.getNodes() ){

                if(v.getNumber() == u.getNumber()){
                    continue;
                }
                if(!essentialGraph.getVertexByNumber(v.getNumber()).isAdjacent(u)){

                    Operation insertion = new Insertion(interventionFamily, essentialGraph, v, u, scoreFunction).propose();
                    if(optimalOperation == null || insertion.getMaxScoreChange() > optimalOperation.getMaxScoreChange()){
                        optimalOperation = insertion;
                    }
                }

            }
        }

        if(optimalOperation != null && optimalOperation.getMaxScoreChange() > SCORE_DIFF_THRESHOLD){

            optimalOperation.commit();

        }
    }

}
