package net.ipsoft.ipcenter.iprca.causality.model.adjImpl;

import net.ipsoft.ipcenter.iprca.causality.graph.algorithm.graphTypeChecking.DagChecker;
import net.ipsoft.ipcenter.iprca.causality.graph.algorithm.transformer.DagGenerator;
import net.ipsoft.ipcenter.iprca.causality.graph.algorithm.transformer.EssentialGraphGenerator;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Dag;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Edge;
import net.ipsoft.ipcenter.iprca.causality.graph.model.EdgeType;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.Intervention;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.InterventionFamily;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 11/18/15.
 */
public class EssentialGraphTest {


    @Test
    public void testDagEssentialGraphConversion() throws Exception {


        Dag dag = getDag();
        DagChecker checker = new DagChecker(dag);

        assertEquals(checker.isTrue(), false);

    }

    @Test
    public void testDagEssentialGraphConversion_1() throws Exception {


        Dag dag = getDag2();
        DagChecker checker = new DagChecker(dag);

        assertEquals(checker.isTrue(), true);

        EssentialGraphGenerator eg = new EssentialGraphGenerator(dag);
        eg.toEssentialGraph();

        ChainGraph ed0 = dag.deepCopy();
        DagGenerator dg = new DagGenerator(dag);

        dg.toDag();

        EssentialGraphGenerator eg1 = new EssentialGraphGenerator(dag);
        eg1.toEssentialGraph();
        
        assertEquals(ed0.hasSameEdges(dag), true);



    }

    private Dag getDag2(){

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

        return  new Dag(nodes, edges);
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
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS), new Edge(2,5, EdgeType.DIRECTED_PLUS), new Edge(2,6, EdgeType.DIRECTED_MINUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS), new Edge(3,7, EdgeType.DIRECTED_PLUS), new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        return  new Dag(nodes, edges);
    }

    @Test
    public void testEssentialOrdering() throws Exception {

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED),new Edge(1,5, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.UNDIRECTED),new Edge(2,5, EdgeType.UNDIRECTED),new Edge(2,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS),new Edge(3,7, EdgeType.DIRECTED_PLUS),new Edge(3,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,7, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
        );

        ChainGraph ed = new ChainGraph(nodes, edges);
        ed.orderEdge(Arrays.asList(new Node(3),new Node(2),new Node(1),new Node(5)));

        DagChecker checker = new DagChecker(ed);

        assertEquals(checker.isTrue(), true);

    }

    //REF_HB
    @Test
    public void testEssentialGraphGenerator_fig4a() throws Exception {

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS),new Edge(1,5, EdgeType.DIRECTED_PLUS)
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
    public void testEssentialGraphGenerator_fig4b() throws Exception {

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS),new Edge(1,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,4, EdgeType.DIRECTED_MINUS),new Edge(2,5, EdgeType.DIRECTED_PLUS),new Edge(2,6, EdgeType.DIRECTED_PLUS)
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
        assertEquals(result.noOfUndirectEdge(), 1);


    }


    @Test
    public void testEssentialGraphGenerator_fig4b_outOfOrder() throws Exception {

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(2,1, EdgeType.DIRECTED_PLUS),new Edge(1,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,2, EdgeType.DIRECTED_PLUS),new Edge(4,2, EdgeType.DIRECTED_PLUS),new Edge(2,5, EdgeType.DIRECTED_PLUS),new Edge(2,6, EdgeType.DIRECTED_PLUS)
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
        assertEquals(result.noOfUndirectEdge(), 1);

    }

    //REF_HB
    @Test
    public void testEssentialGraphGenerator_fig5a() throws Exception {

        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS),new Edge(1,5, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS),new Edge(2,5, EdgeType.DIRECTED_PLUS),new Edge(2,6, EdgeType.DIRECTED_PLUS)
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


    @Test
    public void testEssentialGraphGenerator_patternA() throws Exception {
        Dag dag = getSimpleDag();
        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph();

        assertEquals(result.isUnDirectedGraph(), true);
    }

    @Test
    public void testEssentialGraphGenerator_patternB() throws Exception {
        Dag dag = getSimpleV();
        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph();

        assertEquals(result.isDirectedGraph(), true);
    }

    @Test
    public void testEssentialGraphGenerator_patternC() throws Exception {
        Dag dag = getPatternC();
        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph();

        assertEquals(result.isUnDirectedGraph(), true);
    }

    private Dag getPatternC(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS),new Edge(1,3, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS)
        );

        return  new Dag(nodes, edges);
    }


    @Test
    public void testEssentialGraphGenerator_patternD() throws Exception {
        Dag dag = getPatternD();
        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph();

        assertEquals(result.isUnDirectedGraph(), false);
        assertEquals(result.isDirectedGraph(), false);
    }

    private Dag getPatternD(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS),new Edge(1,3, EdgeType.UNDIRECTED),new Edge(1,4, EdgeType.UNDIRECTED)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,4, EdgeType.DIRECTED_MINUS)
        );

        return  new Dag(nodes, edges);
    }


    @Test
    public void testEssentialGraphGenerator_allProtected() throws Exception {
        Dag dag = getAllProtected();
        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph();

        assertEquals(result.isUnDirectedGraph(), false);
        assertEquals(result.isDirectedGraph(), true);
    }

    private Dag getAllProtected(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,4, EdgeType.DIRECTED_PLUS)
                ,new Edge(3,4, EdgeType.DIRECTED_PLUS)
        );

        return  new Dag(nodes, edges);
    }


    @Test
    public void testEssentialGraphGenerator_intervention() throws Exception {
        Dag dag = getintervention();

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));

        InterventionFamily invf = new InterventionFamily(Arrays.asList(inv0, inv1));

        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph(invf);

        assertEquals(result.isUnDirectedGraph(), false);
        assertEquals(result.isDirectedGraph(), false);
    }


    @Test
    public void testEssentialGraphGenerator_intervention_2() throws Exception {
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.UNDIRECTED),new Edge(1,3, EdgeType.DIRECTED_MINUS),new Edge(1,4, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS),new Edge(2,4, EdgeType.DIRECTED_MINUS)
                ,new Edge(3,4, EdgeType.DIRECTED_MINUS)
        );

        ChainGraph ed = new ChainGraph(nodes, edges);

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(4)));
        InterventionFamily interventionFamily = new InterventionFamily(Arrays.asList(inv0, inv1));

        EssentialGraphGenerator generator = new EssentialGraphGenerator(ed);
        ChainGraph result = generator.toEssentialGraph(interventionFamily);

        assertEquals(result.isUnDirectedGraph(), false);
        assertEquals(result.isDirectedGraph(), false);
    }


    @Test
    public void testEssentialGraphGenerator_3nodes() throws Exception {
        int noOfNodes = 3;
        List<Node> nodes = new ArrayList<>();
        for(int i = 1; i <= noOfNodes; i++){
            Node node = new Node(i);
            node.addLevel("0","1");
            nodes.add(node);
        }

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS), new Edge(2,3, EdgeType.DIRECTED_PLUS), new Edge(1,3, EdgeType.DIRECTED_PLUS)
        );

        Dag dag = new Dag(nodes, edges);
        Intervention inv0 = new Intervention(Arrays.asList());

        InterventionFamily invf = new InterventionFamily(Arrays.asList(inv0));

        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph(invf);

        assertEquals(result.isUnDirectedGraph(), true);
        assertEquals(result.isDirectedGraph(), false);
    }



    @Test
    public void testEssentialGraphGenerator_intervention2() throws Exception {
        Dag dag = getintervention();

        Intervention inv0 = new Intervention(Arrays.asList());
        Intervention inv1 = new Intervention( Arrays.asList(new Node(1), new Node(2)));

        InterventionFamily invf = new InterventionFamily(Arrays.asList(inv0, inv1));

        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph(invf);

        assertEquals(result.isUnDirectedGraph(), false);
        assertEquals(result.isDirectedGraph(), false);
    }


    @Test
    public void testEssentialGraphGenerator_observe() throws Exception {
        Dag dag = getintervention();

        Intervention inv0 = new Intervention(Arrays.asList());

        InterventionFamily invf = new InterventionFamily(Arrays.asList(inv0));

        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph(invf);

        assertEquals(result.isUnDirectedGraph(), true);
    }

    private Dag getintervention(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3), new Node(4), new Node(5), new Node(6), new Node(7)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS),
                new Edge(3,4, EdgeType.DIRECTED_PLUS)
                ,new Edge(4,5, EdgeType.DIRECTED_PLUS)
                ,new Edge(5,6, EdgeType.DIRECTED_PLUS)
                ,new Edge(6,7, EdgeType.DIRECTED_PLUS)
        );

        return  new Dag(nodes, edges);
    }


    private Dag getSimpleV(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS)
        );

        return  new Dag(nodes, edges);
    }

    private Dag getSimpleDag(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_PLUS)
                ,new Edge(2,3, EdgeType.DIRECTED_PLUS)
        );

        return  new Dag(nodes, edges);
    }

    private Dag getSimpleDag_other(){
        List<Node> nodes = Arrays.asList(new Node(1), new Node(2), new Node(3)
        );

        List<Edge> edges = Arrays.asList(
                new Edge(1,2, EdgeType.DIRECTED_MINUS)
                ,new Edge(2,3, EdgeType.DIRECTED_MINUS)
        );

        return  new Dag(nodes, edges);
    }


    @Test
    public void testEssentialGraphGenerator_patternA_other() throws Exception {
        Dag dag = getSimpleDag_other();
        EssentialGraphGenerator generator = new EssentialGraphGenerator(dag);
        ChainGraph result = generator.toEssentialGraph();

        assertEquals(result.isUnDirectedGraph(), true);
    }

}
