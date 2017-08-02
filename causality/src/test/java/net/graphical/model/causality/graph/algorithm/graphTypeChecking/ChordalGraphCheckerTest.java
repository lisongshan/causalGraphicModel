package net.graphical.model.causality.graph.algorithm.graphTypeChecking;

import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.algorithm.ordering.PeoChecker;
import net.graphical.model.causality.graph.model.AdjImpl.ChordalGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;
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
public class ChordalGraphCheckerTest {

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
    public void testPeoCheck() throws Exception {

        ChordalGraph graph = CreateChordalGraph();

        List<Vertex> orders = graph.getLexOrdering();
        String orderString = orders.stream().map(v->String.valueOf(v.getVertexNumber())).reduce((s1,s2)->s1+s2).get();

        assertEquals("1256347", orderString);

        PeoChecker peoChecker = new PeoChecker(graph);
        boolean isPeo = peoChecker.isTrue(orders);
        assertEquals(true, isPeo);


    }

    @Test
    public void testPeoCheck_nonChordal() throws Exception {

        ChordalGraph graph = CreateNonChordalGraph();

        List<Vertex> orders = graph.getLexOrdering();
        String orderString = orders.stream().map(v->String.valueOf(v.getVertexNumber())).reduce((s1,s2)->s1+s2).get();

        assertEquals("1253647", orderString);

        PeoChecker peoChecker = new PeoChecker(graph);
        boolean isPeo = peoChecker.isTrue(orders);
        assertEquals(false, isPeo);


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



    private ChordalGraph CreateNonChordalGraph(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
                ,new Node(4),new Node(5),new Node(6),new Node(7)

        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED), new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED),new Edge(2,5, EdgeType.UNDIRECTED)
                ,new Edge(3,4, EdgeType.UNDIRECTED),new Edge(3,7, EdgeType.UNDIRECTED),new Edge(3,6, EdgeType.UNDIRECTED)
                ,new Edge(4,7, EdgeType.UNDIRECTED)
                ,new Edge(5,6, EdgeType.UNDIRECTED)
        );

        return  new ChordalGraph(nodes, edges);

    }

}
