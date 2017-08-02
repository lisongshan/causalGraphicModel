package net.graphical.model.causality.learning.gies;

import net.graphical.model.causality.interventionData.dataGenerator.CausalModel;
import net.graphical.model.causality.graph.algorithm.graphTypeChecking.DagChecker;
import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;
import net.graphical.model.causality.interventionData.DataSet;
import net.graphical.model.causality.interventionData.dataGenerator.OneCpd;
import net.graphical.model.causality.utils.EmpiricalDistribution;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 3/2/16.
 */
public class causalModelTest {

    @Test
    public void testCausalModelCompareWithEmpiricalDistribution() throws Exception {

        Intervention intervention = new Intervention(Arrays.asList());

        List<Intervention> interventions = Arrays.asList(intervention);

        CausalModel causalModel = new CausalModel(getDag(), new OneCpd());
        DataSet dataSet = causalModel.generateDataSet(40000, interventions);
        EmpiricalDistribution empiricalDistribution = new EmpiricalDistribution(dataSet, causalModel.getCpd(), causalModel.getDag());

        empiricalDistribution.checkDataSetAgainstCpd(causalModel.getDag().getNodes());
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
        DagChecker checker = new DagChecker(dag);

        assertEquals(checker.isTrue(), true);
        return  dag;
    }

    /**
     * Created by sli on 12/2/15.
     */

}
