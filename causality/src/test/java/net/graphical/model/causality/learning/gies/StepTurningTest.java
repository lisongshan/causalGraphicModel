package net.graphical.model.causality.learning.gies;

import net.graphical.model.causality.graph.algorithm.graphTypeChecking.DagChecker;
import net.graphical.model.causality.graph.algorithm.transformer.EssentialGraphGenerator;
import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;
import net.graphical.model.causality.interventionData.DataSet;
import net.graphical.model.causality.interventionData.dataGenerator.CausalModel;
import net.graphical.model.causality.interventionData.dataGenerator.OneCpd;
import net.graphical.model.causality.scoreFunction.BicScoreFunction;
import net.graphical.model.causality.scoreFunction.ScoreFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 12/7/15.
 */
public class StepTurningTest {
    @Test
    public void testBic_4Variables_autogen_inv0_4000() throws Exception {

        Intervention intervention = new Intervention(Arrays.asList());

        List<Intervention> interventions = Arrays.asList(intervention);


        int noOfNodes = 4;
        List<Node> nodes = new ArrayList<>();
        for(int i = 1; i <= noOfNodes; i++){
            Node node = new Node(i);
            node.addLevel("0","1");
            nodes.add(node);
        }

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS), new Edge(1,3, EdgeType.DIRECTED_PLUS), new Edge(1,4, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_MINUS)
        );

        Dag dag =   new Dag(nodes, edges);
        DagChecker checker = new DagChecker(dag);

        assertEquals(checker.isTrue(), true);

        List<Edge> edges1 = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS), new Edge(1,3, EdgeType.DIRECTED_PLUS), new Edge(1,4, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_MINUS)
        );

        Dag dag1 =   new Dag(nodes, edges1);
        DagChecker checker1 = new DagChecker(dag1);
        assertEquals(checker1.isTrue(), true);

        CausalModel causalModel = new CausalModel(dag, new OneCpd());
        DataSet dataSet = causalModel.generateDataSet(4000, interventions);
        ScoreFunction scoreFunction = new BicScoreFunction(dataSet);


        double score = scoreFunction.score(dag);


        double score2 = scoreFunction.score(dag1);

        ChainGraph ed = dag.deepCopy();
        ed.makeEssential(dataSet.getInterventionFamily());


        ChainGraph ed1 = dag1.deepCopy();
        ed1.makeEssential(dataSet.getInterventionFamily());

        assertEquals(ed.hasSameEdges(ed1), false);

        double score_ed = scoreFunction.score(ed);
        double score_ed1_before = scoreFunction.score(ed1);

        Step step = new StepTurning(ed1, scoreFunction, dataSet.getInterventionFamily());
        step.execute();

        double score_ed1_after = scoreFunction.score(ed1);

        assertEquals(ed.hasSameEdges(ed1), true);


    }






    @Test
    public void testBic_7Variables_autogen_inv0_4000() throws Exception {

        Dag dag2 = getDag2();

        ChainGraph dag2_copy = dag2.deepCopy();
        DagChecker checker  = new DagChecker(dag2);
        assertEquals(checker.isTrue(), true);

        Intervention intervention = new Intervention(Arrays.asList());

        CausalModel causalModel = new CausalModel(dag2, new OneCpd());
        DataSet dataSet = causalModel.generateDataSet(4000, Arrays.asList(intervention));
        ScoreFunction scoreFunction = new BicScoreFunction(dataSet);


        double score_dag2 = scoreFunction.score(dag2);

        EssentialGraphGenerator generator2 = new EssentialGraphGenerator(dag2);

        ChainGraph ed2 = generator2.toEssentialGraph();

        ChainGraph ed2Copy = ed2.deepCopy();


        double score_ed2 = scoreFunction.score(ed2Copy);

        Learner learner = new Learner(dataSet.getColumnNodes(), scoreFunction, dataSet.getInterventionFamily());
        ChainGraph ed2_learned = learner.learnEssentialGraph();
        double score_ed2_learned = scoreFunction.score(ed2_learned);

        double score_exact = scoreFunction.score(dag2);
        double score_exact1 = scoreFunction.score(dag2_copy);


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
                new Edge(1,2, EdgeType.DIRECTED_MINUS), new Edge(1,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS), new Edge(2,5, EdgeType.DIRECTED_PLUS), new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS), new Edge(3,7, EdgeType.DIRECTED_PLUS), new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        return  new Dag(nodes, edges);
    }


}
