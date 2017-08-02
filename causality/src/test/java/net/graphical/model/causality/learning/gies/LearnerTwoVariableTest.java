package net.graphical.model.causality.learning.gies;

import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;
import net.graphical.model.causality.interventionData.DataEntry;
import net.graphical.model.causality.interventionData.DataSet;
import net.graphical.model.causality.interventionData.NodeProb;
import net.graphical.model.causality.interventionData.dataGenerator.CausalModel;
import net.graphical.model.causality.interventionData.dataGenerator.OneCpd;
import net.graphical.model.causality.scoreFunction.BicScoreFunction;
import net.graphical.model.causality.scoreFunction.NodesConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 12/2/15.
 */
public class LearnerTwoVariableTest {

    @Test
    public void testLearner_twoVariables_intervention_0 () throws Exception {

        DataSet dataSet = generationDataSetIntervention_0();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        Assert.assertEquals(edge.getEdgeType(), EdgeType.UNDIRECTED);

    }

    @Test
    public void testLearner_twoVariables_intervention_0_1 () throws Exception {

        DataSet dataSet = generationDataSetIntervention_1();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);

    }

    @Test
    public void testLearner_twoVariables_intervention_0_2 () throws Exception {

        DataSet dataSet = generationDataSetIntervention_2();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);

    }

    @Test
    public void testLearner_twoVariables_withInitial_intervention_0_1 () throws Exception {

        DataSet dataSet = generationDataSetIntervention_1();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);

        ChainGraph g0 = new ChainGraph(dataSet.getColumnNodes(), Arrays.asList(new Edge(dataSet.getColumnNode(1), dataSet.getColumnNode(2), EdgeType.DIRECTED_MINUS)));
        Learner learner = new Learner(g0, localScore, dataSet.getInterventionFamily());


        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);

    }

    @Test
    public void testLearner_twoVariables_withInitial_intervention_0_2 () throws Exception {

        DataSet dataSet = generationDataSetIntervention_2();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);

        ChainGraph g0 = new ChainGraph(dataSet.getColumnNodes(), Arrays.asList(new Edge(dataSet.getColumnNode(1), dataSet.getColumnNode(2), EdgeType.DIRECTED_MINUS)));
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
     * P(X2=0) = p*q2 + (1-p)*r2, P(X2=1) = p(1-q2) + (1-p) *(1-r2);
     */

    public DataSet generationDataSetIntervention_0() throws Exception
    {
        double p=0.5, q2=0.2, r2=0.8;

        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.addAll(generationDataEntries_twoBinaryVariables_observation(300, p, q2, r2));

        Node x1Node = new Node(1);
        x1Node.addLevel("0","1");

        Node x2Node = new Node(2);
        x2Node.addLevel("0","1");

        List<Node> columnNodes = Arrays.asList(x1Node, x2Node);

        return new DataSet(columnNodes, dataEntries);

    }


    public DataSet generationDataSetIntervention_1() throws Exception
    {
        double p=0.5, q2=0.2, r2=0.8;

        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.addAll(generationDataEntries_twoBinaryVariables_observation(300, p, q2, r2));
        dataEntries.addAll(generationDataEntries_twoBinaryVariables_intervention_1(300, p, q2, r2));

        Node x1Node = new Node(1);
        x1Node.addLevel("0","1");

        Node x2Node = new Node(2);
        x2Node.addLevel("0","1");

        List<Node> columnNodes = Arrays.asList(x1Node, x2Node);

        return new DataSet(columnNodes, dataEntries);

    }



    public DataSet generationDataSetIntervention_2() throws Exception
    {
        double p=0.5, q2=0.2, r2=0.8;

        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.addAll(generationDataEntries_twoBinaryVariables_observation(300, p, q2, r2));
        dataEntries.addAll(generationDataEntries_twoBinaryVariables_intervention_2(300, p, q2, r2));

        Node x1Node = new Node(1);
        x1Node.addLevel("0","1");

        Node x2Node = new Node(2);
        x2Node.addLevel("0","1");

        List<Node> columnNodes = Arrays.asList(x1Node, x2Node);

        return new DataSet(columnNodes, dataEntries);

    }


    public List<DataEntry> generationDataEntries_twoBinaryVariables_observation(int count, double p, double q2, double r2) throws Exception
    {
        List<DataEntry> dataEntries = new ArrayList<>();

        Intervention observation = new Intervention(Arrays.asList());

        //generate ten observation data
        for(int i = 0; i < count; i++){
            DataEntry e1 = new DataEntry(observation, 2);
            SecureRandom rd = new SecureRandom();
            String x1="0";
            String x2="0";
            if(rd.nextDouble() > p){
                x1 = "1";
            }

            if("0".equals(x1) && rd.nextDouble() > q2){
                x2 = "1";
            }

            if("1".equals(x1) && rd.nextDouble() > r2){
                x2 = "1";
            }

            e1.addData(1, x1);
            e1.addData(2, x2);
            dataEntries.add(e1);
        }

        return dataEntries;

    }



    public List<DataEntry> generationDataEntries_twoBinaryVariables_intervention_1(int count, double p, double q2, double r2) throws Exception
    {
        List<DataEntry> dataEntries = new ArrayList<>();

        Intervention intervention = new Intervention(Arrays.asList(new Node(1)));

        //generate ten interventional data : I = {1};
        // x1="0";
        for(int i = 0; i < count/2; i++){
            DataEntry e1 = new DataEntry(intervention, 2);
            SecureRandom rd = new SecureRandom();

            String x2="0";
            if(rd.nextDouble() > q2){
                x2 = "1";
            }

            e1.addData(1, "0");
            e1.addData(2, x2);
            dataEntries.add(e1);
        }

        // x1="1";
        for(int i = 0; i < count/2; i++){
            DataEntry e1 = new DataEntry(intervention,2);
            SecureRandom rd = new SecureRandom();

            String x2="0";
            if(rd.nextDouble() > r2){
                x2 = "1";
            }

            e1.addData(1, "1");
            e1.addData(2, x2);
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
    public void testLearner_twoVariables_autogen () throws Exception {

        DataSet dataSet = getDataSet_bin();
        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        Edge edge = ed.findEdge(dataSet.getColumnNodes().get(0), dataSet.getColumnNodes().get(1));
        assertEquals(edge.getEdgeType(), EdgeType.DIRECTED_PLUS);

    }

    public DataSet getDataSet_bin () throws Exception {

        Node x1Node = new Node(1);
        x1Node.addLevel("0","1");
        Node x2Node = new Node(2);
        x2Node.addLevel("0","1");

        List<Node> nodes = Arrays.asList(x1Node, x2Node);
        Edge edge = new Edge(x1Node, x2Node, EdgeType.DIRECTED_PLUS);
        List<Edge> edges = Arrays.asList(edge);

        Dag dag = new Dag(nodes, edges);
        OneCpd cpd = getConditionalDistribution (nodes);
        CausalModel causalModel = new CausalModel(dag, cpd);

        Intervention intervention = new Intervention(Arrays.asList());
        List<DataEntry> dataEntries = causalModel.generateDataEntries(intervention, 30);

        Intervention intervention1 = new Intervention(Arrays.asList(new Node(1)));
        List<DataEntry> dataEntries1 = causalModel.generateDataEntries(intervention1, 30);

        dataEntries.addAll(dataEntries1);

        List<Node> columnNodes = Arrays.asList(x1Node, x2Node);
        return new DataSet(columnNodes, dataEntries);
    }

    /**
     * @throws Exception
     * X1->X2
     * P(X1=0) = p; P(X1=1) = 1-p;
     * P(X2=0|X1=0) = q2,P(X2=1|X1=0) = 1- q2,P(X2=0|X1=1) = r2,P(X2=1|X1=1) = r2,
     * P(X2=0) = p*q2 + (1-p)*r2, P(X2=1) = p(1-q2) + (1-p) *(1-r2);
     */


    public OneCpd getConditionalDistribution (List<Node> columnNodes){
        OneCpd cpd = new OneCpd();
        Node x1Node = columnNodes.get(0);
        Node x2Node = columnNodes.get(1);

        NodesConfiguration conf = new NodesConfiguration();
        double[] prob = new double[2];
        prob[0] = 0.5;
        prob[1] = 1 - prob[0];
        NodeProb node1Prob = new NodeProb(x1Node, prob);
        cpd.add(conf, node1Prob);


        NodesConfiguration conf1 = new NodesConfiguration(x1Node, "0");
        NodesConfiguration conf2 = new NodesConfiguration(x1Node, "1");

        double[] prob1 = new double[2];

        // double p=0.5, q2=0.2, r2=0.8;
        prob1[0] = 0.2;
        prob1[1] = 1 - prob1[0];
        NodeProb node2Prob1 = new NodeProb(x2Node, prob1);
        cpd.add(conf1, node2Prob1);

        double[] prob2 = new double[2];

        prob2[0] = 0.8;
        prob2[1] = 1 - prob2[0];

        NodeProb node2Prob2 = new NodeProb(x2Node, prob2);

        cpd.add(conf2, node2Prob2);

        return cpd;
    }
}
