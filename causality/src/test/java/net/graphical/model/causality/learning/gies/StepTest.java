package net.graphical.model.causality.learning.gies;

import net.graphical.model.causality.graph.algorithm.graphTypeChecking.ChainGraphChecker;
import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.AdjImpl.ConnectedUndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;
import net.graphical.model.causality.graph.model.intervention.InterventionFamily;
import net.graphical.model.causality.interventionData.DataSet;
import net.graphical.model.causality.interventionData.dataGenerator.CausalModel;
import net.graphical.model.causality.interventionData.dataGenerator.OneCpd;
import net.graphical.model.causality.learning.gies.operation.Operation;
import net.graphical.model.causality.learning.gies.operation.TurningEssential;
import net.graphical.model.causality.scoreFunction.BicScoreFunction;
import net.graphical.model.causality.scoreFunction.ScoreFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 12/1/15.
 */
public class StepTest {


    @Test
    public void testLearner_3Variables_vstructure_inv0() throws Exception {

        Intervention intervention = new Intervention(Arrays.asList());
        Intervention intervention1 = new Intervention(Arrays.asList(new Node(1)));

        List<Intervention> interventions = Arrays.asList(intervention, intervention1);

        int noOfNodes = 3;
        List<Node> nodes = new ArrayList<>();
        for(int i = 1; i <= noOfNodes; i++){
            Node node = new Node(i);
            node.addLevel("0","1");
            nodes.add(node);
        }

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(2,3, EdgeType.UNDIRECTED)
        );

        Dag dag = new Dag(nodes, edges);

        CausalModel causalModel = new CausalModel(dag, new OneCpd());
        DataSet dataSet = causalModel.generateDataSet(8000, interventions);
        BicScoreFunction localScore = new BicScoreFunction(dataSet);

        StepTurning stepTurning = new StepTurning(dag, localScore, dataSet.getInterventionFamily());

        stepTurning.perturbEssentialGraph();



    }


    //REF_HB
    @Test
    public void testStepForward_Backward() throws Exception {

        ScoreFunction score_Function_insert = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return 5 * parentNodes.size();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        ChainGraph ed = getEssentialD();

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));

        Step stepForward = new StepForward(ed, score_Function_insert, interventionFamily);

        stepForward.execute();

        ChainGraph snapShot = ed.deepCopy();
        List<ConnectedUndirectedGraph> chainComponents = snapShot.getChainComponents();
        assertEquals(chainComponents.size(), 3);
        ChainGraphChecker checker  = new ChainGraphChecker(snapShot);
        assertEquals(checker.isTrue(), true);


        ScoreFunction score_Function_delete = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return -5 * parentNodes.size();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        Step stepBackward = new StepBackward(ed, score_Function_delete, interventionFamily);
        stepBackward.execute();
        ChainGraph snapShot2 = ed.deepCopy();
        ChainGraphChecker checker2  = new ChainGraphChecker(snapShot2);
        assertEquals(checker2.isTrue(), true);

        List<ConnectedUndirectedGraph> chainComponents2 = snapShot2.getChainComponents();
        assertEquals(chainComponents2.size(), 7);


    }


    @Test
    public void testStepTurning_Turning_4_simplified() throws Exception {// 3 nodes will pass.



        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED),new Edge(1,3, EdgeType.UNDIRECTED),new Edge(1,4, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.UNDIRECTED),new Edge(2,4, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS)
        );

        ChainGraph ed = new ChainGraph(nodes, edges);

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));


        ScoreFunction score_Function_turning = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                if(parentNodes.size() > 0){
                    return parentNodes.get(0).getNumber() - node.getNumber();
                }
                return -node.getNumber();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        Operation turningEssential = new TurningEssential(interventionFamily, ed, new Node(4), new Node(1), score_Function_turning).propose();
        turningEssential.commit();

        ChainGraphChecker checker = new ChainGraphChecker(ed);
        assertEquals(checker.isTrue(), true);

        List<ConnectedUndirectedGraph> chainComponents2 = ed.getChainComponents();
        assertEquals(chainComponents2.size(), 3);


    }


    @Test
    public void testStepTurning_Turning_4_simplified_1() throws Exception {// 3 nodes will pass.

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED),new Edge(1,3, EdgeType.DIRECTED_MINUS),new Edge(1,4, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,4, EdgeType.DIRECTED_MINUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS)
        );

        ChainGraph ed = new ChainGraph(nodes, edges);



        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));

        ed.makeEssential(interventionFamily);

        ScoreFunction score_Function_turning = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                if(parentNodes.size() > 0){
                    return parentNodes.get(0).getNumber() - node.getNumber();
                }
                return -node.getNumber();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        Operation turningEssential = new TurningEssential(interventionFamily, ed, new Node(4), new Node(1), score_Function_turning).propose();
        turningEssential.commit();

        ChainGraphChecker checker = new ChainGraphChecker(ed);
        assertEquals(checker.isTrue(), true);

        List<ConnectedUndirectedGraph> chainComponents2 = ed.getChainComponents();
        assertEquals(chainComponents2.size(), 3);


    }

    @Test
    public void testStepTurning_Turning_4_simplified_failed() throws Exception {// 3 nodes will pass.

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED),new Edge(1,3, EdgeType.DIRECTED_MINUS),new Edge(1,4, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,4, EdgeType.DIRECTED_MINUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS)
        );

        ChainGraph ed = new ChainGraph(nodes, edges);



        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));

        ed.makeEssential(interventionFamily);

        ScoreFunction score_Function_turning = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                if(parentNodes.size() > 0){
                    return parentNodes.get(0).getNumber() - node.getNumber();
                }
                return -node.getNumber();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        Operation turningEssential = new TurningEssential(interventionFamily, ed, new Node(2), new Node(3), score_Function_turning).propose();
        turningEssential.commit();

        ChainGraphChecker checker = new ChainGraphChecker(ed);
        assertEquals(checker.isTrue(), true);

        List<ConnectedUndirectedGraph> chainComponents2 = ed.getChainComponents();
        assertEquals(chainComponents2.size(), 3);


    }


    @Test
    public void testStepForward_Turning_4() throws Exception {// 3 nodes will pass.

        ScoreFunction score_Function_insert = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return 5 * parentNodes.size();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4)
        );
        ChainGraph ed = new ChainGraph(nodes, Arrays.asList());

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));

        Step stepForward = new StepForward(ed, score_Function_insert, interventionFamily);

        stepForward.execute();

        ChainGraph snapShot = ed.deepCopy();
        List<ConnectedUndirectedGraph> chainComponents = snapShot.getChainComponents();
        assertEquals(chainComponents.size(), 2);
        ChainGraphChecker checker  = new ChainGraphChecker(snapShot);
        assertEquals(checker.isTrue(), true);


        ScoreFunction score_Function_turning = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                if(parentNodes.size() > 0){
                    return parentNodes.get(0).getNumber() - node.getNumber();
                }
                return -node.getNumber();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        Step stepTurning = new StepTurning(ed, score_Function_turning, interventionFamily);
        stepTurning.execute();

        ChainGraph snapShot2 = ed.deepCopy();
        List<ConnectedUndirectedGraph> chainComponents2 = snapShot2.getChainComponents();
        assertEquals(chainComponents2.size(), 2);

        ChainGraphChecker checker2  = new ChainGraphChecker(snapShot2);
        assertEquals(checker2.isTrue(), true);
    }


    //REF_HB
    @Test
    public void testStepForward_Turning_5() throws Exception {

        ScoreFunction score_Function_insert = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return 5 * parentNodes.size();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5)
        );
        ChainGraph ed = new ChainGraph(nodes, Arrays.asList());

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));

        Step stepForward = new StepForward(ed, score_Function_insert, interventionFamily);

        stepForward.execute();
        ChainGraph snapShot = ed.deepCopy();
        List<ConnectedUndirectedGraph> chainComponents = snapShot.getChainComponents();
        assertEquals(chainComponents.size(), 3);
        ChainGraphChecker checker  = new ChainGraphChecker(snapShot);
        assertEquals(checker.isTrue(), true);


        ScoreFunction score_Function_turning = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                if(parentNodes.size() > 0){
                    return parentNodes.get(0).getNumber() - node.getNumber();
                }
                return -node.getNumber();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        Step stepTurning = new StepTurning(ed, score_Function_turning, interventionFamily);
        stepTurning.execute();
        ChainGraph snapShot2 = ed.deepCopy();
        ChainGraphChecker checker2  = new ChainGraphChecker(snapShot2);
        assertEquals(checker2.isTrue(), true);

        List<ConnectedUndirectedGraph> chainComponents2 = snapShot2.getChainComponents();
        assertEquals(chainComponents2.size(), 3);
    }


    //REF_HB
    @Test
    public void testStepForward_Turning_7() throws Exception {

        ScoreFunction score_Function_insert = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return 5 * parentNodes.size();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );
        ChainGraph ed = new ChainGraph(nodes, Arrays.asList());

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));

        Step stepForward = new StepForward(ed, score_Function_insert, interventionFamily);

        stepForward.execute();
        ChainGraph snapshot = ed.deepCopy();
        List<ConnectedUndirectedGraph> chainComponents = snapshot.getChainComponents();
        assertEquals(chainComponents.size(), 3);
        ChainGraphChecker checker  = new ChainGraphChecker(snapshot);
        assertEquals(checker.isTrue(), true);


        ScoreFunction score_Function_turning = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                if(parentNodes.size() > 0){
                    return parentNodes.get(0).getNumber() - node.getNumber();
                }
                return -node.getNumber();
            }

            @Override
            public double score(ChainGraph dag) throws Exception {
                return 0;
            }
        };

        Step stepTurning = new StepTurning(ed, score_Function_turning, interventionFamily);
        stepTurning.execute();

        ChainGraph snapshot2 = ed.deepCopy();
        ChainGraphChecker checker2  = new ChainGraphChecker(snapshot2);
        assertEquals(checker2.isTrue(), true);

        List<ConnectedUndirectedGraph> chainComponents2 = snapshot2.getChainComponents();
        assertEquals(chainComponents2.size(), 3);
    }


    private ChainGraph getEssentialD(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList();

        return new ChainGraph(nodes, edges);
    }
}
