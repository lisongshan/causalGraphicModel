package net.ipsoft.ipcenter.iprca.causality.learning.gies;

import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.InterventionFamily;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.ScoreFunction;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sli on 10/28/15.
 */
public class Learner {


    private ScoreFunction scoreFunction;
    private InterventionFamily interventionFamily;
    ChainGraph essentialGraph;

    public Learner(List<Node> columnNodes, ScoreFunction scoreFunction, InterventionFamily interventionFamily) {
        this(new ChainGraph(columnNodes, Arrays.asList()), scoreFunction, interventionFamily);
    }

    public Learner(ChainGraph essentialGraph, ScoreFunction scoreFunction, InterventionFamily interventionFamily) {
        this.scoreFunction = scoreFunction;
        this.interventionFamily = interventionFamily;
        this.essentialGraph = essentialGraph;
    }


    //Algorithm2
    public ChainGraph learnEssentialGraph() throws Exception {


        Step forwardStep = new StepForward(essentialGraph, scoreFunction, interventionFamily);
        forwardStep.execute();

        ChainGraph snapShot1 = essentialGraph.deepCopy();
        double gfScore = scoreFunction.score(snapShot1);

        Step backStep = new StepBackward(essentialGraph, scoreFunction, interventionFamily);
        backStep.execute();

        ChainGraph snapShot2 = essentialGraph.deepCopy();
       double gbScore = scoreFunction.score(snapShot2);

        if(gbScore < gfScore)
            throw new Exception("gbScore < gfScore");

        Step tunningStep = new StepTurning(essentialGraph, scoreFunction, interventionFamily);
        tunningStep.execute();

        ChainGraph snapShot3 = essentialGraph.deepCopy();
       double gtScore = scoreFunction.score(snapShot3);

        if(gtScore < gbScore)
            throw new Exception("gtScore < gbScore");
        return essentialGraph;

    }


}
