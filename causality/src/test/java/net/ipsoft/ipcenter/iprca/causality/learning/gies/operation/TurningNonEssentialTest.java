package net.ipsoft.ipcenter.iprca.causality.learning.gies.operation;

import net.ipsoft.ipcenter.iprca.causality.graph.algorithm.transformer.EssentialGraphGenerator;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Dag;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Edge;
import net.ipsoft.ipcenter.iprca.causality.graph.model.EdgeType;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.Intervention;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.InterventionFamily;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.ScoreFunction;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 11/30/15.
 */
public class TurningNonEssentialTest {

    //REF_HB
    @Test
    public void testTurningNonEssential_propose() throws Exception {

        ScoreFunction scoreFunction = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return Math.pow(5, parentNodes.size());
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

        Operation turningNotEssential = new TurningNotEssential(interventionFamily, ed, new Node(5), new Node(2), scoreFunction).propose();

        assertEquals(turningNotEssential.cliqueArgMaxC.get(0).getNumber(), 3);

    }

    @Test
    public void testTurningNonEssential_commit() throws Exception {

        ScoreFunction scoreFunction = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return Math.pow(5, parentNodes.size());
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

        Operation turningNotEssential = new TurningNotEssential(interventionFamily, ed, new Node(5), new Node(2), scoreFunction).propose();
        turningNotEssential.commit();

        assertEquals(ed.noOfUndirectEdge(), 1);

    }

    //REF_HB
    @Test
    public void testEssentialGraphGenerator_fig6a() throws Exception {

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS),new Edge(1,5, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,5, EdgeType.DIRECTED_PLUS),new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS),new Edge(3,7, EdgeType.DIRECTED_PLUS),new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        Dag dag = new Dag(nodes, edges);

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily invf = new InterventionFamily(Arrays.asList(inv0, inv1));

        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph(invf);


    }

    //REF_HB
    @Test
    public void testEssentialGraphGenerator_fig6b() throws Exception {

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS),new Edge(1,5, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,5, EdgeType.DIRECTED_MINUS),new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS),new Edge(3,7, EdgeType.DIRECTED_PLUS),new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        Dag dag = new Dag(nodes, edges);

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));

        InterventionFamily invf = new InterventionFamily(Arrays.asList(inv0, inv1));

        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph(invf);


    }

    private ChainGraph getEssentialD(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED),new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED),new Edge(2,5, EdgeType.UNDIRECTED),new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS),new Edge(3,7, EdgeType.DIRECTED_PLUS),new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        return new ChainGraph(nodes, edges);
    }
}
