package net.graphical.model.causality.learning.gies.operation;

import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.intervention.InterventionFamily;
import net.graphical.model.causality.scoreFunction.ScoreFunction;
import net.graphical.model.causality.graph.model.AdjImpl.UndirectedGraph;
import net.graphical.model.causality.graph.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by sli on 11/20/15.
 */
public class TurningEssential extends Operation {
    public TurningEssential(InterventionFamily interventionFamily, ChainGraph essentialGraph, Node subjectChild, Node subject, ScoreFunction scoreFunction) throws Exception {
        super(interventionFamily, essentialGraph, subjectChild, subject, scoreFunction);
    }


    private Vertex getSubject(){
        return essentialGraph.getVertexByNumber(this.v.getNumber());
    }

    private Vertex getSubjectChild(){
        return essentialGraph.getVertexByNumber(this.u.getNumber());
    }

    @Override
    public Operation propose() throws Exception {
        // u-> is an edge in essential graph
        Vertex subject = getSubject();
        Vertex subjectChild = getSubjectChild();


        List<Vertex> N = intersect(subject.getNeighborVertexes(), subjectChild.getAdjVertexes());
        List<List<Node>> cliqueClist = getCliques(subject.getNeighborVertexes(), N);
        for(List<Node> cliqueC : cliqueClist){
            if(validate(cliqueC, N, subjectChild, subject)){

                List<Node> parentV0 = union(subject.getParents(), cliqueC);
                List<Node> parentV1 = union(parentV0, Arrays.asList(subjectChild.getNode()));

                List<Node> parentU0 = subjectChild.getParents();
                List<Node> parentU1 = diff(parentU0, Arrays.asList(subject.getNode()));


                double deltS = scoreFunction.getLocalScore(subject.getNode(), parentV1)
                        + scoreFunction.getLocalScore(subjectChild.getNode(), parentU1);


                deltS += -scoreFunction.getLocalScore(subject.getNode(), parentV0)
                        - scoreFunction.getLocalScore(subjectChild.getNode(), parentU0);
                if(deltS > maxScoreChange){
                    maxScoreChange = deltS;
                    cliqueArgMaxC = cliqueC;
                }
            }
        }

        return this;
    }



    protected List<List<Node>> getCliques(List<Vertex> neighborVertexes, List<Vertex> N) {
        List<Node> neigborNodes = neighborVertexes.stream().map(v->v.getNode()).collect(Collectors.toList());
        List<List<Node>>  cliques = essentialGraph.getAllCliques(neigborNodes);
        List<List<Node>> biggerCliques = new ArrayList<>();
        List<Node> Nnodes = N.stream().map(v -> v.getNode()).collect(Collectors.toList());
        for(List<Node> clique : cliques){
            TreeSet<Node> cliqueAsSet = new TreeSet<>(clique);
            if(cliqueAsSet.containsAll(Nnodes)){
                biggerCliques.add(clique);
            }
        }
        return biggerCliques;
    }


    protected boolean validate(List<Node>cliqueC, List<Vertex> N, Vertex subjectChild, Vertex subject) {
//REF_HB proposition 34(iii
// every path from v to u in G except (v, u) has vertex in C U ne(u)
        List<Node> cliqueCunionNeigborv = union(cliqueC, subjectChild.getNeighbors());
        List<Node> nodes = diff(essentialGraph.getNodes(), cliqueCunionNeigborv);
        List<Edge> edges = new ArrayList<>();
        for(Edge edge : essentialGraph.getEdges()){
            if(nodes.contains(edge.getFirstNode()) && nodes.contains(edge.getSecondNode())){
                edges.add(edge);
            }
        }
        edges.remove(new Edge(subject.getNode(), subjectChild.getNode(), EdgeType.DIRECTED_PLUS));

        ChainGraph chainGraph = new ChainGraph(nodes, edges);
        return !chainGraph.isReachable(subject.getNode(), subjectChild.getNode());

    }

    @Override
    public void commit() {

        try {

            if(cliqueArgMaxC == null){
                return;
            }

            List<Node> orderingFromChainComponentUmax = getOrderingFromChainComponentSubjectChild();

            List<Node> orderingFromChainComponentVmax = getOrderingFromChainComponentSubject();

            essentialGraph.orderEdge(orderingFromChainComponentUmax);
            essentialGraph.orderEdge(orderingFromChainComponentVmax);

            essentialGraph.turnDirectedEdge(getSubject().getNode(), getSubjectChild().getNode());
            essentialGraph.makeEssential(interventionFamily);

            validate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Node> getOrderingFromChainComponentSubjectChild() throws Exception {
        UndirectedGraph chainComponent = essentialGraph.getChainComponent(this.getSubjectChild().getNode());
        List<Node> initialOrdering = new ArrayList<>();
        initialOrdering.add(this.getSubjectChild().getNode());
        for(Node node: chainComponent.getNodes()){
            if(!initialOrdering.contains(node)){
                initialOrdering.add(node);
            }
        }

        List<Vertex> initialVertexOrdering = initialOrdering.stream().map(n -> chainComponent.getVertexByNumber(n.getNumber())).collect(Collectors.toList());


        List<Vertex> ordering_tU = chainComponent.getLexOrdering(initialVertexOrdering);

        return ordering_tU.stream().map(v->v.getNode()).collect(Collectors.toList());
    }

    public List<Node> getOrderingFromChainComponentSubject() throws Exception {
        UndirectedGraph chainComponent = essentialGraph.getChainComponent(getSubject().getNode());
        List<Node> initialOrdering = new ArrayList<>();
        initialOrdering.addAll(cliqueArgMaxC);
        initialOrdering.add(getSubject().getNode());
        for(Node node: chainComponent.getNodes()){
            if(!initialOrdering.contains(node)){
                initialOrdering.add(node);
            }
        }

        List<Vertex> initialVertexOrdering = initialOrdering.stream().map(n -> chainComponent.getVertexByNumber(n.getNumber())).collect(Collectors.toList());


        List<Vertex> ordering = chainComponent.getLexOrdering(initialVertexOrdering);

        return ordering.stream().map(v->v.getNode()).collect(Collectors.toList());
    }
}
