package net.graphical.model.causality.model.adjImpl;

import net.graphical.model.causality.graph.model.AdjImpl.UndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 11/23/15.
 */
public class UndirectedGraphTest {





    @Test
    public void testGetAllClique_0() throws Exception {
        UndirectedGraph graph = getUnDirectedGraph_0();

        List<List<Node>> cliques = graph.getAllCliques();
        assertEquals(4, cliques.size());

    }

    @Test
    public void testGetAllMaximalClique_simple_0() throws Exception {
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1, 2, EdgeType.UNDIRECTED)
                , new Edge(2, 3, EdgeType.UNDIRECTED)
        );

        UndirectedGraph graph = new UndirectedGraph(nodes, edges);
        List<List<Node>> cliques = graph.getAllMaxmalCliques_standard();
        List<List<Node>> cliques1 = graph.getAllMaxmalCliques_pivot();
        assertEquals(cliques1.size(), cliques.size());

    }

    @Test
    public void testGetAllMaximalClique_probe_0() throws Exception {
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                , new Node(4), new Node(5)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1, 2, EdgeType.UNDIRECTED), new Edge(1, 3, EdgeType.UNDIRECTED)
                , new Edge(2, 3, EdgeType.UNDIRECTED), new Edge(2, 5, EdgeType.UNDIRECTED)
                , new Edge(3, 4, EdgeType.UNDIRECTED)
        );

        UndirectedGraph graph = new UndirectedGraph(nodes, edges);
        List<List<Node>> cliques = graph.getAllMaxmalCliques_standard();
//        List<List<Node>> cliques1 = graph.getAllMaxmalCliques_pivot();
//        assertEquals(cliques1.size(), cliques.size());

    }

    @Test
    public void testGetAllMaximalClique_probe_1() throws Exception {
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                , new Node(4), new Node(5)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1, 2, EdgeType.UNDIRECTED), new Edge(1, 3, EdgeType.UNDIRECTED)
                , new Edge(2, 3, EdgeType.UNDIRECTED), new Edge(2, 5, EdgeType.UNDIRECTED)
                , new Edge(3, 4, EdgeType.UNDIRECTED)
        );

        UndirectedGraph graph = new UndirectedGraph(nodes, edges);
        List<Node> R = Arrays.asList(new Node(1));
        List<Node> P = Arrays.asList(new Node(2), new Node(3));
        List<Node> X = Arrays.asList();

        List<List<Node>> result = new ArrayList<>();

        graph.doBronKerbosch_standard(R, new TreeSet<>(P), new TreeSet<>(X), result);


    }


    @Test
    public void testGetAllMaximalClique_probe_2() throws Exception {
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                , new Node(4), new Node(5)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1, 2, EdgeType.UNDIRECTED), new Edge(1, 3, EdgeType.UNDIRECTED)
                , new Edge(2, 3, EdgeType.UNDIRECTED), new Edge(2, 5, EdgeType.UNDIRECTED)
                , new Edge(3, 4, EdgeType.UNDIRECTED)
        );

        UndirectedGraph graph = new UndirectedGraph(nodes, edges);
        List<Node> R = Arrays.asList(new Node(2));
        List<Node> P = Arrays.asList(new Node(3), new Node(5));
        List<Node> X = Arrays.asList(new Node(1));;

        List<List<Node>> result = new ArrayList<>();

        graph.doBronKerbosch_standard(R, new TreeSet<>(P), new TreeSet<>(X), result);


    }

    @Test
    public void testGetAllMaximalClique_probe_3() throws Exception {
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                , new Node(4), new Node(5)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1, 2, EdgeType.UNDIRECTED), new Edge(1, 3, EdgeType.UNDIRECTED)
                , new Edge(2, 3, EdgeType.UNDIRECTED), new Edge(2, 5, EdgeType.UNDIRECTED)
                , new Edge(3, 4, EdgeType.UNDIRECTED)
        );

        UndirectedGraph graph = new UndirectedGraph(nodes, edges);
        List<Node> R = Arrays.asList();
        List<Node> P = Arrays.asList(new Node(2), new Node(1), new Node(5), new Node(3), new Node(4));
        List<Node> X = Arrays.asList();

        List<List<Node>> result = new ArrayList<>();

        graph.doBronKerbosch_standard(R, new TreeSet<>(P), new TreeSet<>(X), result);


    }


    @Test
    public void testGetAllMaximalClique_0() throws Exception {
        UndirectedGraph graph = getUnDirectedGraph_0();

        List<List<Node>> cliques = graph.getAllMaxmalCliques_standard();
        assertEquals(1, cliques.size());

    }





    @Test
    public void testGetAllClique_1() throws Exception {
        UndirectedGraph graph = getUnDirectedGraph_1();

        List<List<Node>> cliques = graph.getAllCliques();
        assertEquals(14, cliques.size());

    }


    @Test
    public void testGetAllMaximalClique_1() throws Exception {
        UndirectedGraph graph = getUnDirectedGraph_1();

        List<List<Node>> cliques = graph.getAllMaxmalCliques_standard();

        List<List<Node>> cliques1 = graph.getAllMaxmalCliques_pivot();

        assertEquals(3, cliques.size());
        assertEquals(3, cliques1.size());

    }


    @Test
    public void testGetAllMaximalClique_1_subNodes() throws Exception {
        UndirectedGraph graph = getUnDirectedGraph_1();

        Vertex v2 = graph.getVertexByNumber(2);

        List<List<Node>> cliques = graph.getAllMaximalCliques(v2.getNeighbors());


        assertEquals(3, cliques.size());

    }


    @Test
    public void testGetAllMaximalClique_pivot_1() throws Exception {
        UndirectedGraph graph = getUnDirectedGraph_1();

        List<List<Node>> cliques1 = graph.getAllMaxmalCliques_pivot();

        assertEquals(3, cliques1.size());

    }

    @Test
    public void testGetAllMaximalClique_2() throws Exception {
        UndirectedGraph graph = getUnDirectedGraph_2();

        List<List<Node>> cliques = graph.getAllMaxmalCliques_standard();
        List<List<Node>> cliques1 = graph.getAllMaxmalCliques_pivot();

        assertEquals(cliques1.size(), cliques.size());

    }


    @Test
    public void testGetAllClique_2() throws Exception {
        UndirectedGraph graph = getUnDirectedGraph_2();

        List<List<Node>> cliques = graph.getAllCliques();
        assertEquals(18, cliques.size());

    }

    private UndirectedGraph getUnDirectedGraph_0(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED)
        );

        return  new UndirectedGraph(nodes, edges);
    }


    private UndirectedGraph getUnDirectedGraph_1(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,4, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED), new Edge(2,4, EdgeType.UNDIRECTED), new Edge(2,5, EdgeType.UNDIRECTED)
                ,new Edge(4,5, EdgeType.UNDIRECTED)
        );

        return  new UndirectedGraph(nodes, edges);
    }

    private UndirectedGraph getUnDirectedGraph_2(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,4, EdgeType.UNDIRECTED), new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED), new Edge(2,4, EdgeType.UNDIRECTED), new Edge(2,5, EdgeType.UNDIRECTED)
                ,new Edge(4,5, EdgeType.UNDIRECTED)
        );

        return  new UndirectedGraph(nodes, edges);
    }



}
