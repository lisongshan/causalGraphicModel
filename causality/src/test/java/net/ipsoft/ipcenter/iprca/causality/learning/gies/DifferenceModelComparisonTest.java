package net.ipsoft.ipcenter.iprca.causality.learning.gies;

import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Dag;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Vertex;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Edge;
import net.ipsoft.ipcenter.iprca.causality.graph.model.EdgeType;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.Intervention;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;
import net.ipsoft.ipcenter.iprca.causality.interventionData.dataGenerator.CausalModel;
import net.ipsoft.ipcenter.iprca.causality.interventionData.dataGenerator.OneCpd;
import net.ipsoft.ipcenter.iprca.causality.model.ConditionalDistribution;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.BicScoreFunction;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.NodesConfiguration;
import net.ipsoft.ipcenter.iprca.causality.utils.CpdTableStore;
import net.ipsoft.ipcenter.iprca.causality.utils.EmpiricalDistribution;
import org.junit.Test;

import java.util.*;

/**
 * Created by sli on 3/7/16.
 */
public class DifferenceModelComparisonTest {

    @Test
    public void testLearner_DiffferenceModel_autogen_inv0_2 () throws Exception {

        Intervention intervention = new Intervention(Arrays.asList());

        List<Intervention> interventions = Arrays.asList(intervention);

        Dag dag = getDiffDag();

        List<Node> ordering = dag.topologicalOrdering();

        ConditionalDistribution cpd = new OneCpd();

        for(Node node : ordering){
            Vertex vertex = dag.getVertexByNumber(node.getNumber());

            List<Node> parents = vertex.getParents();
            List<NodesConfiguration> parentNodesConfigs = NodesConfiguration.generateNodesConfiguration(new TreeSet<>(parents));

            for(NodesConfiguration nodesConfiguration : parentNodesConfigs){
                cpd.getProb(node, nodesConfiguration);
            }
        }


        CausalModel causalModel = new CausalModel(dag, cpd);

        CpdTableStore store = new CpdTableStore(new Date().getTime() + "_cpd.txt");
        store.save(cpd.getCpdTable());

        DataSet dataSet = causalModel.generateDataSet(8000, interventions);

        EmpiricalDistribution empiricalDistribution = new EmpiricalDistribution(dataSet, causalModel.getCpd(), causalModel.getDag());

        empiricalDistribution.checkDataSetAgainstCpd(causalModel.getDag().getNodes());

        BicScoreFunction localScore = new BicScoreFunction(dataSet);
        Learner learner = new Learner(dataSet.getColumnNodes(), localScore, dataSet.getInterventionFamily());
        ChainGraph ed = learner.learnEssentialGraph();
        dataSet.getColumnNodes().forEach(n->System.out.println(n.getDescription()));
        System.out.println(ed.getEdges().toString());

    }


    private Dag getDiffDag(){

        List<Node> totalNodes = new ArrayList<>();
        List<Node> stateChangeNodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        node1.setDescription("A"+ "__" + "a");
        node2.setDescription("B"+ "__"+ "b");
        node3.setDescription("C"+ "__" + "c");
        node4.setDescription("D"+ "__" + "d");

        stateChangeNodes.addAll(Arrays.asList(new Node[]{node1, node2, node3, node4,}));


        totalNodes.addAll(stateChangeNodes);


        List<Edge> edgeList = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS), new Edge(1,3, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,4, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS)
        );
        edges.addAll(edgeList);

        List<Node> stateNodes = new ArrayList<>();
        for(Node stateChangeNode : stateChangeNodes){
            Node stateNode = new Node(stateChangeNode.getNumber()+4);
            stateNode.setDescription("state" + stateChangeNode.getDescription());
            stateChangeNode.setDescription("stateChange" + stateChangeNode.getDescription());
            Edge edge = new Edge(stateNode.getNumber(), stateChangeNode.getNumber(),  EdgeType.DIRECTED_PLUS);

            stateNodes.add(stateNode);

            edges.add(edge);
        }
        totalNodes.addAll(stateNodes);

        for(int i = 0; i < totalNodes.size()/2; i++){

            totalNodes.get(i).addLevel("KK","KC","CC","CK");
        }

        for(int i = totalNodes.size()/2; i < totalNodes.size(); i++){

            totalNodes.get(i).addLevel("K","C");
        }

        totalNodes.forEach(n->System.out.println(n.getDescription()));
        System.out.println(edges.toString());

        Dag dag = new Dag(totalNodes, edges);

        return dag;
    }

}
