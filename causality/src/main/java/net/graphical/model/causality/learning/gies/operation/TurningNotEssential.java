package net.graphical.model.causality.learning.gies.operation;

import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.AdjImpl.UndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.InterventionFamily;
import net.graphical.model.causality.scoreFunction.ScoreFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sli on 11/20/15.
 */
public class TurningNotEssential extends Operation {
    public TurningNotEssential(InterventionFamily interventionFamily, ChainGraph essentialGraph, Node subjectChild, Node subject, ScoreFunction scoreFunction) throws Exception {
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
        // v->u is an edge in the essential graph
        Vertex subject = getSubject();
        Vertex subjectParent = getSubjectChild();


        List<Vertex> N = intersect(subject.getNeighborVertexes(), subjectParent.getAdjVertexes());
        List<Node> Nnodes = N.stream().map(v1-> v1.getNode()).collect(Collectors.toList());

        List<List<Node>> cliqueClist = getCliques(subject.getNeighborVertexes(), subjectParent);
        for(List<Node> cliqueC : cliqueClist){

            if(validate(cliqueC, Nnodes, subjectParent, subject)){


                List<Node> parentV0 = union(subject.getParents(), cliqueC);
                List<Node> parentV1 = union(parentV0, Arrays.asList(subjectParent.getNode()));


                List<Node> parentU1 = intersect(cliqueC, Nnodes);
                parentU1 = union(subjectParent.getParents(), parentU1);

                List<Node> parentU0 = union(parentU1, Arrays.asList(subject.getNode()));

                double deltS = scoreFunction.getLocalScore(subject.getNode(), parentV1)
                        + scoreFunction.getLocalScore(subjectParent.getNode(), parentU1);


                deltS += -scoreFunction.getLocalScore(subject.getNode(), parentV0)
                        - scoreFunction.getLocalScore(subjectParent.getNode(), parentU0);
                if(deltS > maxScoreChange){
                    maxScoreChange = deltS;
                    cliqueArgMaxC = cliqueC;
                }
            }
        }

        return this;
    }

    protected List<List<Node>> getCliques(List<Vertex> neighborVertexes, Vertex u) {
        neighborVertexes.remove(u);
        List<Node> nodes = neighborVertexes.stream().map(v->v.getNode()).collect(Collectors.toList());
       return essentialGraph.getAllCliques(nodes);
    }

    /**
     *
     * @param cliqueC
     * @param Nnodes
     * @param subjectParent
     * @param subject
     * @return
     */
    protected boolean validate(List<Node>cliqueC, List<Node> Nnodes, Vertex subjectParent, Vertex subject) {

        //C\N != 0 and {u,v} separates C and N\C in Chain(v)????not sure it is the same as propostion 31 (iii): REF_HB  proposition 31 (ii) (iii)
        List<Node> c_n = diff(cliqueC, Nnodes);
        if(c_n.isEmpty())
            return false;


        List<Node> n_c = diff(Nnodes, cliqueC);

        //contruct inducedG of G[Ne(v)-C/\N]

        List<Node> cliqueC_CandN = diff(cliqueC, intersect(cliqueC, Nnodes));
        List<Edge> edges = new ArrayList<>();
        for(Edge edge : essentialGraph.getEdges()){
            if(cliqueC_CandN.contains(edge.getFirstNode()) && cliqueC_CandN.contains(edge.getSecondNode())){
                edges.add(edge);
            }
        }

        UndirectedGraph ug = new UndirectedGraph(cliqueC_CandN, edges);

        List<Node> _c_n = diff(ug.getNodes(), c_n);
        List<Node> _n_c = diff(ug.getNodes(), n_c);

        for( Node v1 : _c_n){
            for(Node v2 : _n_c){
                if( connected_dfs(v1, v2, ug)){
                    return false;
                }
            }
        }

        return true;
    }

    private boolean connected_dfs(Node v1, Node v2, UndirectedGraph ug) {
        Vertex vertex1 = ug.getVertexByNumber(v1.getNumber());
        vertex1.setIsExplored(true);
        for(Vertex v : vertex1.getNeighborVertexes()){
            if(v.getNode().getNumber() ==  v2.getNumber())
                return true;
            if(!v.isExplored()){
                return connected_dfs(v.getNode(), v2, ug);
            }
        }
        return false;
    }




    @Override
    public void commit() {

        try {

            if(cliqueArgMaxC == null){
                return;
            }

            List<Node> orderingFromChainComponentV = getOrderingFromChainComponentV();

            essentialGraph.orderEdge(orderingFromChainComponentV);
            essentialGraph.turnDirectedEdge(getSubject().getNode(), getSubjectChild().getNode());
            essentialGraph.makeEssential(interventionFamily);

            validate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Node> getOrderingFromChainComponentV() throws Exception {
        UndirectedGraph chainComponent = essentialGraph.getChainComponent(getSubject().getNode());
        List<Node> initialOrdering = new ArrayList<>();
        initialOrdering.addAll(cliqueArgMaxC);
        initialOrdering.add(getSubject().getNode());
        initialOrdering.add(getSubjectChild().getNode());

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
