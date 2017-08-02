package net.graphical.model.causality.model.adjImpl;

import net.graphical.model.causality.graph.model.AdjImpl.*;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.algorithm.graphTypeChecking.DagChecker;
import net.graphical.model.causality.graph.model.Node;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 10/29/15.
 */
public class ChainGraphTest {


    @Test
    public void testGetConnectedComponents_undirectedGraph() throws Exception {
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1, 2, EdgeType.UNDIRECTED), new Edge(1, 5, EdgeType.UNDIRECTED)
                , new Edge(2, 3, EdgeType.UNDIRECTED), new Edge(2, 5, EdgeType.UNDIRECTED)
        );

        UndirectedGraph ug = new UndirectedGraph(nodes, edges);
        List<ConnectedUndirectedGraph> connectedComponents = ug.getConnectedComponents();
        assertEquals(connectedComponents.size(), 4);
    }

    @Test
    public void testGetChainComponents() throws Exception {
        ChainGraph ed = getEssentialD();
        List<ConnectedUndirectedGraph> chainComponents = ed.getChainComponents();

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

    @Test
    public void testDagGetParents() throws Exception {
        Dag dag = getDag();
        List<Node> parents_7 = dag.getVertexByNumber(7).getParents();
        List<Node> children_2 = dag.getVertexByNumber(2).getChildren();
        List<Node> neighbors_5 = dag.getVertexByNumber(5).getNeighbors();


    }



    @Test
    public void testIsDirectedGraph() throws Exception {
        Dag dag = getDag();
        assertEquals(dag.isDirectedGraph(), true);
    }


    @Test
    public void testIsDag() throws Exception {
        GraphBase dag = getDag();
        DagChecker checker = new DagChecker(dag);

        assertEquals(checker.isTrue(), true);
    }

    @Test
    public void testSCC() throws Exception {
        GraphBase dag = getDirectedGraph();
        DagChecker checker = new DagChecker(dag);
        List<List<Vertex>> componentes = checker.getStrongConnectedComponents();
        assertEquals(3, componentes.size());
        assertEquals(1, componentes.get(0).size());
        assertEquals(1, componentes.get(1).size());
        assertEquals(5, componentes.get(2).size());

        assertEquals(checker.isTrue(), false);
    }


    private Dag getDag(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                ,new Node(4), new Node(5), new Node(6), new Node(7)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS), new Edge(1,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS), new Edge(2,5, EdgeType.DIRECTED_PLUS), new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS), new Edge(3,7, EdgeType.DIRECTED_PLUS), new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        return  new Dag(nodes, edges);
    }

    private GraphBase getDirectedGraph(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                ,new Node(4), new Node(5), new Node(6), new Node(7)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS), new Edge(1,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS), new Edge(2,5, EdgeType.DIRECTED_PLUS), new Edge(2,6, EdgeType.DIRECTED_MINUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS), new Edge(3,7, EdgeType.DIRECTED_PLUS), new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        return  new GraphBase(nodes, edges);
    }


    @Test
    public void testGraphGetParents() throws Exception {
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                ,new Node(4), new Node(5), new Node(6), new Node(7)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED), new Edge(2,5, EdgeType.UNDIRECTED), new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS), new Edge(3,7, EdgeType.DIRECTED_PLUS), new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        GraphBase graph = new GraphBase(nodes, edges);

        List<Node> parents_7 = graph.getVertexByNumber(7).getParents();
        List<Node> children_2 = graph.getVertexByNumber(2).getChildren();
        List<Node> neighbors_5 = graph.getVertexByNumber(5).getNeighbors();

    }


    @Test
    public void testDagGetChildren() throws Exception {

        Dag graph = getDag();

        for(Vertex v : graph.getVertexes()){
            List<Node> children = v.getChildren();
        }

    }

}
