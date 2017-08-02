package net.graphical.model.causality.model.adjImpl;

import net.graphical.model.causality.graph.algorithm.ordering.LexBfs;
import net.graphical.model.causality.graph.algorithm.ordering.PeoChecker;
import net.graphical.model.causality.graph.model.AdjImpl.ChordalGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.AdjImpl.UndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 10/29/15.
 */
public class OrderingTest {




    private UndirectedGraph getUndirectedGraph_1(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                ,new Node(4)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,3, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED)
                ,new Edge(3,4, EdgeType.UNDIRECTED)
        );

        return  new UndirectedGraph(nodes, edges);


    }


    private UndirectedGraph getUndirectedGraph_2(){
        List<Node> nodes = Arrays.asList(new Node(3), new Node(2), new Node(1)
                ,new Node(4), new Node(5)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,3, EdgeType.UNDIRECTED), new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED)
                ,new Edge(3,4, EdgeType.UNDIRECTED),new Edge(3,5, EdgeType.UNDIRECTED)
                ,new Edge(4,5, EdgeType.UNDIRECTED)
        );

        return  new UndirectedGraph(nodes, edges);


    }



    @Test
    public void testLexBfsOrdering() throws Exception {

        UndirectedGraph graph = getUndirectedGraph_1();
        LexBfs lex = new LexBfs(graph);
        lex.lexBfsOrdering();


    }

    @Test
    public void testLexBfsOrderingWithInitialSequence() throws Exception {

        ChordalGraph graph = CreateChordalGraph();
        List<Integer> init = Arrays.asList(6,3,1,2,4,5,7);
        List<Vertex> iniitSeq = init.stream().map(i-> graph.getVertexByNumber(i.intValue())).collect(Collectors.toList());

        List<Vertex> orders = graph.getLexOrdering(iniitSeq);
        String orderString = orders.stream().map(v->String.valueOf(v.getVertexNumber())).reduce((s1,s2)->s1+s2).get();

        assertEquals(orderString, "6325471");

        PeoChecker peoChecker = new PeoChecker(graph);
        boolean isPeo = peoChecker.isTrue(orders);
        assertEquals(isPeo, true);


    }


    @Test
    public void testDagTopologicalOrdering() throws Exception {

        //REF_HB, figure 17 B
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                ,new Node(4),new Node(5),new Node(6),new Node(7)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS), new Edge(1,5, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,6, EdgeType.DIRECTED_MINUS),new Edge(2,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS),new Edge(3,7, EdgeType.DIRECTED_PLUS),new Edge(3,6, EdgeType.DIRECTED_MINUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_MINUS)
        );

        Dag dag = new Dag(nodes, edges);
        List<Node> orders = dag.topologicalOrdering();
        String orderString = orders.stream().map(v->String.valueOf(v.getNumber())).reduce((s1,s2)->s1+s2).get();

        assertEquals(orderString, "6347251");

    }


    @Test
    public void testLexBfsOrdering_2() throws Exception {

        UndirectedGraph graph = getUndirectedGraph_2();
        LexBfs lex = new LexBfs(graph);
        lex.lexBfsOrdering();


    }

//Graph in figure 17, page 2445; REF_HB
    private ChordalGraph CreateChordalGraph(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                ,new Node(4),new Node(5),new Node(6),new Node(7)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED),new Edge(2,6, EdgeType.UNDIRECTED),new Edge(2,5, EdgeType.UNDIRECTED)
                ,new Edge(3,4, EdgeType.UNDIRECTED),new Edge(3,7, EdgeType.UNDIRECTED),new Edge(3,6, EdgeType.UNDIRECTED)
                ,new Edge(4,7, EdgeType.UNDIRECTED)
                ,new Edge(5,6, EdgeType.UNDIRECTED)
        );

        return  new ChordalGraph(nodes, edges);

    }




}
