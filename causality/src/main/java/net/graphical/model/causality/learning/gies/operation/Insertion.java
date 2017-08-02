package net.graphical.model.causality.learning.gies.operation;

import net.graphical.model.causality.graph.model.intervention.InterventionFamily;
import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.AdjImpl.UndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.scoreFunction.ScoreFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by sli on 11/20/15.
 */
public class Insertion extends Operation {
    public Insertion(InterventionFamily interventionFamily, ChainGraph essentialGraph, Node u, Node v, ScoreFunction scoreFunction) throws Exception {
        super(interventionFamily, essentialGraph, u, v, scoreFunction);

    }

    private Node getU(){
        return u;
    }

    private Node getV(){
        return v;
    }

    @Override
    public void commit() {
        try {
            if(cliqueArgMaxC == null){
                return;
            }

            essentialGraph.orderEdge(getOrderingFromChainComponentV ());
            essentialGraph.addDirectedEdge(getU(), getV());
            essentialGraph.makeEssential(interventionFamily);
            validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Node> getOrderingFromChainComponentV() throws Exception {
        UndirectedGraph chainComponent = essentialGraph.getChainComponent(this.getV());
        List<Node> initialOrdering = new ArrayList<>();
        initialOrdering.addAll (getCliqueArgMaxC());
        initialOrdering.add(getV());
        for(Node node: chainComponent.getNodes()){
            if(!initialOrdering.contains(node)){
                initialOrdering.add(node);
            }
        }

        List<Vertex> initialVertexOrdering = initialOrdering.stream().map(n -> chainComponent.getVertexByNumber(n.getNumber())).collect(Collectors.toList());

        List<Vertex> ordering = chainComponent.getLexOrdering(initialVertexOrdering);

        return ordering.stream().map(v->v.getNode()).collect(Collectors.toList());
    }

    @Override
    public Operation propose() throws Exception {
        // v and u are not adjacent to each other
        Vertex v = essentialGraph.getVertexByNumber(this.v.getNumber());
        Vertex u = essentialGraph.getVertexByNumber(this.u.getNumber());

        List<Vertex> N = intersect(v.getNeighborVertexes(), u.getAdjVertexes());
        List<List<Node>> cliqueClist = getCliques(v.getNeighborVertexes(), N);
        for(List<Node> cliqueC : cliqueClist){

            if(validate(cliqueC, v, u)){
                List<Node> parent1 = union(v.getParents(), cliqueC, Arrays.asList(u.getNode()));
                List<Node> parent0 = union(v.getParents(), cliqueC);

                double deltS = scoreFunction.getLocalScore(v.getNode(), parent1)
                        - scoreFunction.getLocalScore(v.getNode(), parent0);
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


    protected boolean validate(List<Node> cliqueC, Vertex v, Vertex u) {

        //not exist path from v to u which doesn't go through cliqueC
        ChainGraph gMinusC = essentialGraph.subtract(cliqueC);

        return !gMinusC.isReachable(v.getNode(),u.getNode());
    }

}
