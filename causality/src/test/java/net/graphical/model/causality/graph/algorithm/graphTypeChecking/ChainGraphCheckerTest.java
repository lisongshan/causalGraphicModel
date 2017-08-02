package net.graphical.model.causality.graph.algorithm.graphTypeChecking;

import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 12/1/15.
 */
public class ChainGraphCheckerTest {

    @Test
    public void testChainGraph_positive() throws Exception {

        ChainGraph graph = CreateChainGraph();

        ChainGraphChecker cgc = new ChainGraphChecker(graph);
        assertEquals(cgc.isTrue(), true);
    }
    //REF_HB, figure 16
    private ChainGraph CreateChainGraph(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                , new Node(4), new Node(5), new Node(6), new Node(7)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED),new Edge(2,5, EdgeType.UNDIRECTED),new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS),new Edge(3,7, EdgeType.DIRECTED_PLUS),new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.UNDIRECTED)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        return  new ChainGraph(nodes, edges);

    }

    @Test
    public void testChainGraph_negative() throws Exception {

        ChainGraph graph = CreateNonChainGraph();

        ChainGraphChecker cgc = new ChainGraphChecker(graph);
        assertEquals(cgc.isTrue(), false);
    }
    //REF_HB, figure 16, reverse 3 and 7
    private ChainGraph CreateNonChainGraph(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                , new Node(4), new Node(5), new Node(6), new Node(7)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED),new Edge(2,5, EdgeType.UNDIRECTED),new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS),new Edge(3,7, EdgeType.DIRECTED_MINUS),new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.UNDIRECTED)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        return  new ChainGraph(nodes, edges);

    }
}
