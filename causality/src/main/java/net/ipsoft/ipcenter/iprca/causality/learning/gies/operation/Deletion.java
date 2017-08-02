package net.ipsoft.ipcenter.iprca.causality.learning.gies.operation;

import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.UndirectedGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Vertex;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.InterventionFamily;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.ScoreFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sli on 11/20/15.
 */
public class Deletion extends Operation {
    // REF_HB: u-> v is an edge in essentialGraph here v is called subject vertex, u is the parent of the subject;
    public Deletion(InterventionFamily interventionFamily, ChainGraph essentialGraph, Node subjectParent, Node subject, ScoreFunction scoreFunction) throws Exception {
        super(interventionFamily, essentialGraph, subjectParent, subject, scoreFunction);
    }

    private Vertex getSubjectVertex(){
        return essentialGraph.getVertexByNumber(this.v.getNumber());
    }

    private Vertex getParentVertex(){
        return essentialGraph.getVertexByNumber(this.u.getNumber());
    }

    @Override
    public void commit() {

        try {
            if(cliqueArgMaxC == null){
                return;
            }
            essentialGraph.orderEdge(getOrderingFromChainComponentSubject());
            essentialGraph.removeEdge(getParentVertex().getNode(), getSubjectVertex().getNode());
            essentialGraph.makeEssential(interventionFamily);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Node> getOrderingFromChainComponentSubject() throws Exception {
        UndirectedGraph ug = essentialGraph.getChainComponent(getSubjectVertex().getNode());
        List<Node> initialOrdering = new ArrayList<>();
        initialOrdering.addAll (getCliqueArgMaxC());
        if(essentialGraph.getVertexByNumber(getSubjectVertex().getNode().getNumber()).getNeighbors().contains(getParentVertex().getNode())){
            initialOrdering.add (getParentVertex().getNode());
            initialOrdering.add (getSubjectVertex().getNode());
        }
        else
        {
            initialOrdering.add (getSubjectVertex().getNode());
        }

        for(Node node: ug.getNodes()){
            if(!initialOrdering.contains(node)){
                initialOrdering.add(node);
            }
        }
        List<Vertex> initialVertexOrdering = initialOrdering.stream().map(n -> ug.getVertexByNumber(n.getNumber())).collect(Collectors.toList());

        List<Vertex> ordering = ug.getLexOrdering(initialVertexOrdering);

        return ordering.stream().map(v->v.getNode()).collect(Collectors.toList());
    }



    @Override
    public Operation propose() throws Exception {
        // u-> v is an edge in essentialGraph
        Vertex subject = getSubjectVertex();
        Vertex subjectParent = getParentVertex();


        List<Vertex> N = intersect(subject.getNeighborVertexes(), subjectParent.getAdjVertexes());
        List<List<Node>> cliqueClist = getCliques(N);
        for(List<Node> cliqueC : cliqueClist){

            List<Node> parent1 = union(subject.getParents(), cliqueC);
            parent1 = diff(parent1, Arrays.asList(subjectParent.getNode()));

            List<Node> parent0 = union(subject.getParents(), cliqueC);
            parent0 = union(parent0, Arrays.asList(subjectParent.getNode()));

            double deltS = scoreFunction.getLocalScore(subject.getNode(), parent1)
                    - scoreFunction.getLocalScore(subject.getNode(), parent0);
            if(deltS > maxScoreChange){
                maxScoreChange = deltS;
                cliqueArgMaxC = cliqueC;
            }

        }
        return this;
    }

    protected List<List<Node>> getCliques(List<Vertex> N) {
        List<Node> nodes = N.stream().map(v->v.getNode()).collect(Collectors.toList());
        List<List<Node>>  cliques = essentialGraph.getAllCliques(nodes);

        return cliques;
    }


}
