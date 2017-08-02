package net.ipsoft.ipcenter.iprca.causality.learning.gies;

import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.InterventionFamily;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.ScoreFunction;

/**
 * Created by sli on 10/28/15.
 *
 * Algorithm 3
 */
public abstract class Step {
    protected ChainGraph essentialGraph;
    protected ScoreFunction scoreFunction;

    protected InterventionFamily interventionFamily;

    protected static double SCORE_DIFF_THRESHOLD = 0;

    public Step(ChainGraph essentialGraph, ScoreFunction scoreFunction, InterventionFamily interventionFamily) {
        this.essentialGraph = essentialGraph;
        this.scoreFunction = scoreFunction;
        this.interventionFamily = interventionFamily;
    }

    public void execute() throws Exception {

        boolean keepGoing = true;
        ChainGraph g0 = essentialGraph.deepCopy();
        while(keepGoing){

            perturbEssentialGraph();
            if(essentialGraph.hasSameEdges(g0)){
                keepGoing = false;
            }
            else
            {
                g0 = essentialGraph.deepCopy();
            }
        }

    }

    protected abstract  void perturbEssentialGraph() throws Exception;

}
