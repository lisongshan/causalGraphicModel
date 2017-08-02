package net.graphical.model.causality.learning.gies.operation;

import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.intervention.InterventionFamily;
import net.graphical.model.causality.scoreFunction.ScoreFunction;
import net.graphical.model.causality.graph.algorithm.graphTypeChecking.DagChecker;
import net.graphical.model.causality.graph.algorithm.transformer.EssentialGraphGenerator;
import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 11/30/15.
 */
public class TurningEssentialTest {




    //REF_HB
    @Test
    public void testTurningEssential_propose_cliqueCEmpty() throws Exception {

        ScoreFunction scoreFunction = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return -Math.pow(5, parentNodes.size());
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

        Operation turningEssential = new TurningEssential(interventionFamily, ed, new Node(1), new Node(2), scoreFunction).propose();

        assertEquals(turningEssential.cliqueArgMaxC.size(), 0);

    }

    @Test
    public void testTurningEssential_commit_cliqueCEmpty() throws Exception {

        ScoreFunction scoreFunction = new ScoreFunction() {
            @Override
            public double getLocalScore(Node node, List<Node> parentNodes) {
                return -Math.pow(5, parentNodes.size());
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

        Operation turningEssential = new TurningEssential(interventionFamily, ed, new Node(1), new Node(2), scoreFunction).propose();
        turningEssential.commit();

        assertEquals(ed.noOfUndirectEdge(), 0);
        assertEquals(ed.noOfDirectEdge(), 6);

        DagChecker checker = new DagChecker(ed);
        assertEquals(checker.isTrue(), true);


    }


    @Test
    public void testTurningEssential_commit_clique_3() throws Exception {


        ChainGraph ed = getEssentialD();

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));

        Operation turningEssential = new TurningEssential(interventionFamily, ed, new Node(1), new Node(2), null);
        turningEssential.setCliqueArgMaxC(Arrays.asList(new Node(3)));
        turningEssential.commit();

        assertEquals(ed.noOfUndirectEdge(), 0);
        assertEquals(ed.noOfDirectEdge(), 6);

        DagChecker checker = new DagChecker(ed);
        assertEquals(checker.isTrue(), true);


    }

    //REF_HB
    @Test
    public void testEssentialGraphGenerator_fig7b() throws Exception {

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS),new Edge(1,5, EdgeType.DIRECTED_PLUS),new Edge(1,4, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,5, EdgeType.DIRECTED_PLUS)
        );

        Dag dag = new Dag(nodes, edges);

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));

        InterventionFamily invf = new InterventionFamily(Arrays.asList(inv0, inv1));

        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph(invf);


    }

    private ChainGraph getEssentialD(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS),new Edge(1,4, EdgeType.DIRECTED_MINUS),new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED),new Edge(2,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,5, EdgeType.DIRECTED_PLUS)
        );

        return new ChainGraph(nodes, edges);
    }
}
