package net.graphical.model.causality.learning.gies;

import net.graphical.model.causality.interventionData.dataGenerator.CausalModel;
import net.graphical.model.causality.scoreFunction.BicScoreFunction;
import net.graphical.model.causality.scoreFunction.NodesConfiguration;
import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;
import net.graphical.model.causality.interventionData.DataEntry;
import net.graphical.model.causality.interventionData.DataSet;
import net.graphical.model.causality.interventionData.NodeProb;
import net.graphical.model.causality.interventionData.dataGenerator.OneCpd;
import net.graphical.model.causality.utils.DataSetStore;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 12/2/15.
 */
public class LearnerThreeVariableTest {


    @Test
    public void testManualGeneratedObservationDataSet_3Nodes () throws Exception {

        DataSet dataSet = generationDataSetIntervention_0(3000);

        DataSetStore dataStore = new DataSetStore(new Date().getTime() + "_ds.txt");
        dataStore.save(dataSet);


        BicScoreFunction scoreFunction = new BicScoreFunction(dataSet);
        double s1 = scoreFunction.getLocalScore(dataSet.getColumnNode(1), Arrays.asList(dataSet.getColumnNode(2)));

        Node node1 = dataSet.getColumnNode(1);
        Node node2 = dataSet.getColumnNode(2);
        Node node3 = dataSet.getColumnNode(3);
        NodeProb np = scoreFunction.getEmpiricalProb(node1, new NodesConfiguration());
        NodeProb np2_10 = scoreFunction.getEmpiricalProb(node2, new NodesConfiguration(node1, "0"));
        NodeProb np2_11 = scoreFunction.getEmpiricalProb(node2, new NodesConfiguration(node1, "1"));

        NodeProb np3_20 = scoreFunction.getEmpiricalProb(node3, new NodesConfiguration(node2, "0"));
        NodeProb np3_21 = scoreFunction.getEmpiricalProb(node3, new NodesConfiguration(node2, "1"));
    }


    @Test
    public void testManualGeneratedHybridDataSet_3Nodes () throws Exception {

        DataSet dataSet = generationDataSetIntervention_1();

        DataSetStore dataStore = new DataSetStore(new Date().getTime() + "_ds.txt");
        dataStore.save(dataSet);


        BicScoreFunction scoreFunction = new BicScoreFunction(dataSet);
        double s1 = scoreFunction.getLocalScore(dataSet.getColumnNode(1), Arrays.asList(dataSet.getColumnNode(2)));

        Node node1 = dataSet.getColumnNode(1);
        Node node2 = dataSet.getColumnNode(2);
        Node node3 = dataSet.getColumnNode(3);
        NodeProb np = scoreFunction.getEmpiricalProb(node1, new NodesConfiguration());
        NodeProb np2_10 = scoreFunction.getEmpiricalProb(node2, new NodesConfiguration(node1, "0"));
        NodeProb np2_11 = scoreFunction.getEmpiricalProb(node2, new NodesConfiguration(node1, "1"));

        NodeProb np3_20 = scoreFunction.getEmpiricalProb(node3, new NodesConfiguration(node2, "0"));
        NodeProb np3_21 = scoreFunction.getEmpiricalProb(node3, new NodesConfiguration(node2, "1"));
    }

    @Test
    public void testLearner_ThreeVariables_intervention_0 () throws Exception {

        DataSet dataSet = generationDataSetIntervention_0(300);
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.UNDIRECTED);
        Edge edge2 = ed.findEdge(dataSet.getColumnNodes().get(1), dataSet.getColumnNodes().get(2));
        assertEquals(edge.getEdgeType(), EdgeType.UNDIRECTED);

    }

    @Test
    public void testLearner_ThreeVariables_intervention_0_1 () throws Exception {

        DataSet dataSet = generationDataSetIntervention_1();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);

    }

    @Test
    public void testLearner_ThreeVariables_dataSet_load () throws Exception {
        DataSetStore store = new DataSetStore("loadTest_ds.txt");
        DataSet ds = store.load();

    }


    @Test
    public void testLearner_ThreeVariables_withInitial_intervention_0_1 () throws Exception {
        DataSet dataSet = generationDataSetIntervention_1();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);

        List<Edge> edges = new ArrayList<>();
        edges.addAll(Arrays.asList(new Edge(dataSet.getColumnNode(1), dataSet.getColumnNode(2), EdgeType.DIRECTED_PLUS)));
        edges.add(new Edge(dataSet.getColumnNode(2), dataSet.getColumnNode(3), EdgeType.DIRECTED_MINUS));
        ChainGraph g0 = new ChainGraph(dataSet.getColumnNodes(), edges);
        Learner learner = new Learner(g0, localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNode(2), dataSet.getColumnNode(3));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);


    }


    @Test
    public void testLearner_ThreeVariables_withInitial_intervention_0_2 () throws Exception {

        DataSet dataSet = generationDataSetIntervention_2();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);

        ChainGraph g0 = new ChainGraph(dataSet.getColumnNodes(), Arrays.asList(new Edge(dataSet.getColumnNode(1), dataSet.getColumnNode(2), EdgeType.DIRECTED_MINUS)));
        ChainGraph snapshot = g0.deepCopy();
        Learner learner = new Learner(g0, localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);

    }

    /**
     * @throws Exception
     * X1->X2
     * P(X1=0) = p; P(X1=1) = 1-p;
     * P(X2=0|X1=0) = q2,P(X2=1|X1=0) = 1- q2,P(X2=0|X1=1) = r2,P(X2=1|X1=1) = 1-r2,
     * P(X3=0|X2=0) = q3,P(X3=1|X2=0) = 1- q3,P(X3=0|X2=1) = r3,P(X3=1|X2=1) = 1-r3,
     *
     * P(X2=0) = p*q2 + (1-p)*r2, P(X2=1) = p(1-q2) + (1-p) *(1-r2);
     */

    public DataSet generationDataSetIntervention_0(int count) throws Exception
    {
        double p=0.5, q2=0.2, r2=0.8, q3=0.6, r3=0.4;

        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.addAll(generationDataEntries_threeBinaryVariables_observation(count, p, q2, r2, q3, r3));

        Node x1Node = new Node(1);
        x1Node.addLevel("0","1");

        Node x2Node = new Node(2);
        x2Node.addLevel("0","1");

        Node x3Node = new Node(3);
        x3Node.addLevel("0","1");

        List<Node> columnNodes = Arrays.asList(x1Node, x2Node, x3Node);

        return new DataSet(columnNodes, dataEntries);

    }

    @Test
    public void TestDataSetStoreSave() throws Exception {
        DataSet ds = generationDataSetIntervention_1();
        DataSetStore dg = new DataSetStore("test_ds.txt");
        dg.save(ds);
    }



    public DataSet generationDataSetIntervention_1() throws Exception
    {
        double p=0.5, q2=0.2, r2=0.8, q3=0.6, r3=0.4;

        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.addAll(generationDataEntries_threeBinaryVariables_observation(50, p, q2, r2, q3, r3));
        dataEntries.addAll(generationDataEntries_threeBinaryVariables_intervention_1(50, p, q2, r2, q3, r3));

        Node x1Node = new Node(1);
        x1Node.addLevel("0","1");

        Node x2Node = new Node(2);
        x2Node.addLevel("0","1");
        Node x3Node = new Node(3);
        x3Node.addLevel("0","1");

        List<Node> columnNodes = Arrays.asList(x1Node, x2Node, x3Node);

        DataSet ds =  new DataSet(columnNodes, dataEntries);



        return ds;

    }



    public DataSet generationDataSetIntervention_2() throws Exception
    {
        double p=0.5, q2=0.2, r2=0.8, q3=0.6, r3=0.4;

        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.addAll(generationDataEntries_threeBinaryVariables_observation(300, p, q2, r2, q3, r3));
        dataEntries.addAll(generationDataEntries_twoBinaryVariables_intervention_2(300, p, q2, r2));

        Node x1Node = new Node(1);
        x1Node.addLevel("0","1");

        Node x2Node = new Node(2);
        x2Node.addLevel("0","1");

        List<Node> columnNodes = Arrays.asList(x1Node, x2Node);

        return new DataSet(columnNodes, dataEntries);

    }


    public List<DataEntry> generationDataEntries_threeBinaryVariables_observation(int count, double p, double q2, double r2, double q3, double r3) throws Exception
    {
        List<DataEntry> dataEntries = new ArrayList<>();

        Intervention observation = new Intervention(Arrays.asList());

        //generate ten observation data
        for(int i = 0; i < count; i++){
            DataEntry e1 = new DataEntry(observation, 3);
            SecureRandom rd = new SecureRandom();

            String x1="0";
            String x2="0";
            String x3="0";
            if(rd.nextDouble() > p){
                x1 = "1";
            }

            if("0".equals(x1) && rd.nextDouble() > q2){
                x2 = "1";
            }

            if("1".equals(x1) && rd.nextDouble() > r2){
                x2 = "1";
            }

            if("0".equals(x2) && rd.nextDouble() > q3){
                x3 = "1";
            }

            if("1".equals(x2) && rd.nextDouble() > r3){
                x3 = "1";
            }

            e1.addData(1, x1);
            e1.addData(2, x2);
            e1.addData(3, x3);
            dataEntries.add(e1);
        }

        return dataEntries;

    }


    public List<DataEntry> generationDataEntries_threeBinaryVariables_intervention_1(int count, double p, double q2, double r2, double q3, double r3) throws Exception
    {
        List<DataEntry> dataEntries = new ArrayList<>();

        Intervention intervention = new Intervention(Arrays.asList(new Node(1)));

        //generate ten interventional data : I = {1};
        for(int i = 0; i < count; i++){
            DataEntry e1 = new DataEntry(intervention, 3);
            SecureRandom rd = new SecureRandom();

            String x1="0";
            String x2="0";
            String x3="0";
            if(rd.nextDouble() > p){
                x1 = "1";
            }

            if("0".equals(x1) && rd.nextDouble() > q2){
                x2 = "1";
            }

            if("1".equals(x1) && rd.nextDouble() > r2){
                x2 = "1";
            }

            if("0".equals(x2) && rd.nextDouble() > q3){
                x3 = "1";
            }

            if("1".equals(x2) && rd.nextDouble() > r3){
                x3 = "1";
            }

            e1.addData(1, x1);
            e1.addData(2, x2);
            e1.addData(3, x3);
            dataEntries.add(e1);
        }

        return dataEntries;

    }


    public List<DataEntry> generationDataEntries_twoBinaryVariables_intervention_2(int count, double p, double q2, double r2) throws Exception
    {
        List<DataEntry> dataEntries = new ArrayList<>();

        Intervention intervention = new Intervention(Arrays.asList(new Node(2)));

        //generate ten interventional data : I = {2};

        for(int i = 0; i < count/2; i++){
            DataEntry e1 = new DataEntry(intervention, 2);
            SecureRandom rd = new SecureRandom();

            String x2="0";

            String x1="0";
            if(rd.nextDouble() > p){
                x1 = "1";
            }
            e1.addData(1, x1);
            e1.addData(2, x2);
            dataEntries.add(e1);
        }

        for(int i = 0; i < count/2; i++){
            DataEntry e1 = new DataEntry(intervention,2);
            SecureRandom rd = new SecureRandom();

            String x2="1";

            String x1="0";
            if(rd.nextDouble() > p){
                x1 = "1";
            }
            e1.addData(1, x1);
            e1.addData(2, x2);
            dataEntries.add(e1);
        }

        return dataEntries;

    }

    @Test
    public void testLearner_ThreeVariables_autogen_inv0 () throws Exception {

        Intervention intervention = new Intervention(Arrays.asList());

        List<Intervention> interventions = Arrays.asList(intervention);

        DataSet dataSet = getDataSet_ternary(2000, interventions);
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.UNDIRECTED);
        Edge edge2 = ed.findEdge(dataSet.getColumnNodes().get(1), dataSet.getColumnNodes().get(2));
        assertEquals(edge2.getEdgeType(), EdgeType.UNDIRECTED);

    }

    @Test
    public void testLearner_ThreeVariables_autogen_inv0_1 () throws Exception {

        Intervention intervention = new Intervention(Arrays.asList());

        Intervention intervention1 = new Intervention(Arrays.asList(new Node(1)));
        List<Intervention> interventions = Arrays.asList(intervention, intervention1);

        DataSet dataSet = getDataSet_ternary(2000, interventions);
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);

    }


    @Test
    public void testLearner_ThreeVariables_autogen_inv0_2 () throws Exception {

        Intervention intervention = new Intervention(Arrays.asList());

        Intervention intervention1 = new Intervention(Arrays.asList(new Node(2)));
        List<Intervention> interventions = Arrays.asList(intervention, intervention1);

        DataSet dataSet = getDataSet_ternary(2000, interventions);
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);

    }

    public DataSet getDataSet_ternary (int count, List<Intervention> interventions) throws Exception {

        int noOfNodes = 3;
        List<Node> nodes = new ArrayList<>();
        for(int i = 1; i <= noOfNodes; i++){
            Node node = new Node(i);
            node.addLevel("0","1");
            nodes.add(node);
        }
        Edge edge = new Edge(nodes.get(0), nodes.get(1), EdgeType.DIRECTED_PLUS);
        Edge edge2 = new Edge(nodes.get(1), nodes.get(2), EdgeType.DIRECTED_PLUS);
        List<Edge> edges = Arrays.asList(edge, edge2);

        Dag dag = new Dag(nodes, edges);
        OneCpd cpd = getConditionalDistribution (nodes);
        CausalModel causalModel = new CausalModel(dag, cpd);

        List<DataEntry> dataEntries = new ArrayList<>();
        for(Intervention intervention : interventions){
            List<DataEntry> dataEntrysPartial = causalModel.generateDataEntries(intervention, count/interventions.size());
            dataEntries.addAll(dataEntrysPartial);
        }


        dataEntries.addAll(dataEntries);

        return new DataSet(nodes, dataEntries);
    }

    /**
     * 1->2->3
     * @return
     */
    public OneCpd getConditionalDistribution (List<Node> columnNodes){
        OneCpd cpd = new OneCpd();
        Node x1Node = columnNodes.get(0);
        Node x2Node = columnNodes.get(1);
        Node x3Node = columnNodes.get(2);

        //specify for node1
        NodesConfiguration conf = new NodesConfiguration();
        double[] prob = new double[2];
        prob[0] = 0.5;
        prob[1] = 1 - prob[0];
        NodeProb nodeProb = new NodeProb(x1Node, prob);
        cpd.add(conf, nodeProb);


        //specify for node2
        NodesConfiguration conf2_1 = new NodesConfiguration(x1Node, "0");

        double[] prob1 = new double[2];

        // double p=0.5, q2=0.2, r2=0.8;
        prob1[0] = 0.2;
        prob1[1] = 1 - prob1[0];
        NodeProb nodeProb2_1 = new NodeProb(x2Node, prob1);
        cpd.add(conf2_1, nodeProb2_1);

        NodesConfiguration conf2_2 = new NodesConfiguration(x1Node, "1");
        double[] prob2 = new double[2];

        prob2[0] = 0.8;
        prob2[1] = 1 - prob2[0];

        NodeProb nodeProb2_2 = new NodeProb(x2Node, prob2);

        cpd.add(conf2_2, nodeProb2_2);

        //specify for node3
        NodesConfiguration conf3_1 = new NodesConfiguration(x2Node, "0");
        double[] prob3_1 = new double[2];
        prob3_1[0] = 0.4;
        prob3_1[1] = 1 - prob1[0];
        NodeProb nodeProb3_1 = new NodeProb(x3Node, prob3_1);
        cpd.add(conf3_1, nodeProb3_1);

        NodesConfiguration conf3_2 = new NodesConfiguration(x2Node, "1");
        double[] prob3_2 = new double[2];
        prob3_2[0] = 0.6;
        prob3_2[1] = 1 - prob2[0];
        NodeProb nodeProb3_2 = new NodeProb(x3Node, prob3_2);
        cpd.add(conf3_2, nodeProb3_2);

        return cpd;
    }
}
