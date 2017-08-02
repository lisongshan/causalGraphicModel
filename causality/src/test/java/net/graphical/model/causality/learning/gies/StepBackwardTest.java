package net.graphical.model.causality.learning.gies;

import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.interventionData.dataGenerator.CausalModel;
import net.graphical.model.causality.scoreFunction.BicScoreFunction;
import net.graphical.model.causality.scoreFunction.ScoreFunction;
import net.graphical.model.causality.utils.DataSetStore;
import net.graphical.model.causality.graph.algorithm.graphTypeChecking.DagChecker;
import net.graphical.model.causality.graph.algorithm.transformer.EssentialGraphGenerator;
import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;
import net.graphical.model.causality.interventionData.DataSet;
import net.graphical.model.causality.interventionData.dataGenerator.OneCpd;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 12/7/15.
 */
public class StepBackwardTest {
    @Test
    public void testBic_7Variables_autogen_inv0_4000() throws Exception {

        Intervention intervention = new Intervention(Arrays.asList());

        List<Intervention> interventions = Arrays.asList(intervention);

        Dag dag = getDag();
        CausalModel causalModel = new CausalModel(dag, new OneCpd());
        DataSet dataSet =causalModel.generateDataSet(8000, interventions);
        ScoreFunction localScore = new BicScoreFunction(dataSet);

        EssentialGraphGenerator eg = new EssentialGraphGenerator(dag);
        ChainGraph ed_exact = eg.toEssentialGraph();
        double score_exact = localScore.score(ed_exact);

        Dag dag2 = getDag2();
        EssentialGraphGenerator eg0 = new EssentialGraphGenerator(dag2);
        ChainGraph ed0 = eg.toEssentialGraph();
        ChainGraph ed0_copy = ed0.deepCopy();
        double score_before_learned = localScore.score(ed0);

        Step step = new StepBackward(ed0, localScore, dataSet.getInterventionFamily());
        step.execute();
        double score_after_learned = localScore.score(ed0);
        EssentialGraphGenerator generator2 = new EssentialGraphGenerator(dag2);
        ChainGraph ed2 = generator2.toEssentialGraph();

        double score2 = localScore.score(ed2);

    }


    @Test
    public void testBic_7Variables_fromFile_inv0_4000() throws Exception {

        DataSetStore store = new DataSetStore("stepBackword_7nodes_ds.txt");

        Dag dag = getDag();

        DataSet dataSet = store.load();
        ScoreFunction scoreFunction = new BicScoreFunction(dataSet);

        EssentialGraphGenerator eg = new EssentialGraphGenerator(dag);
        ChainGraph ed_exact = eg.toEssentialGraph();
        double score_exact = scoreFunction.score(ed_exact);

        Dag dag2 = getDag2();
        EssentialGraphGenerator eg0 = new EssentialGraphGenerator(dag2);
        ChainGraph ed0 = eg0.toEssentialGraph();
        ChainGraph ed0_copy = ed0.deepCopy();
        double score_before_learned = scoreFunction.score(ed0);

        Step step = new StepBackward(ed0, scoreFunction, dataSet.getInterventionFamily());
        step.execute();
        double score_after_learned = scoreFunction.score(ed0);
        EssentialGraphGenerator generator2 = new EssentialGraphGenerator(dag2);
        ChainGraph ed2 = generator2.toEssentialGraph();

        double score2 = scoreFunction.score(ed2);

    }



    private Dag getDag(){

        int noOfNodes = 7;
        List<Node> nodes = new ArrayList<>();
        for(int i = 1; i <= noOfNodes; i++){
            Node node = new Node(i);
            node.addLevel("0","1");
            nodes.add(node);
        }

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS), new Edge(1,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS), new Edge(2,5, EdgeType.DIRECTED_PLUS), new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS), new Edge(3,7, EdgeType.DIRECTED_PLUS), new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        Dag dag = new Dag(nodes, edges);
        DagChecker checker  = new DagChecker(dag);
        assertEquals(checker.isTrue(), true);

        return  dag;
    }

    private Dag getDag2(){

        int noOfNodes = 7;
        List<Node> nodes = new ArrayList<>();
        for(int i = 1; i <= noOfNodes; i++){
            Node node = new Node(i);
            node.addLevel("0","1");
            nodes.add(node);
        }

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS), new Edge(1,5, EdgeType.DIRECTED_PLUS), new Edge(1,3, EdgeType.DIRECTED_MINUS), new Edge(1,4, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS), new Edge(2,5, EdgeType.DIRECTED_PLUS), new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS), new Edge(3,7, EdgeType.DIRECTED_PLUS), new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS) ,new Edge(4,5, EdgeType.DIRECTED_PLUS) ,new Edge(4,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        Dag dag = new Dag(nodes, edges);
        DagChecker checker  = new DagChecker(dag);
        assertEquals(checker.isTrue(), true);

        return  dag;
    }
}
