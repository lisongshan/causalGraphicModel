package net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl;


import net.ipsoft.ipcenter.iprca.causality.graph.model.Edge;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.Intervention;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sli on 10/27/15.
 */
public class Dag extends ChainGraph {


    public Dag(List<Node> nodes, List<Edge> edges) {
        super(nodes, edges);

    }

    public List<Node> topologicalOrdering() {
        this.exploreReset();
        order = getNodes().size() - 1;
        ordering = new Node[getNodes().size()];
        for(Vertex v : getVertexes()){
            if(!v.isExplored()) {
                dfs_labeling(v);
            }
        }
        return Arrays.asList(ordering);
    }

    private int order;
    private Node[] ordering;
    private void dfs_labeling(Vertex s){
        s.setIsExplored(true);
        for(Vertex v : s.getChildVertexes()){
            if(!v.isExplored()){
                dfs_labeling(v);
            }
        }
        ordering[order--] = s.getNode();
    }

    public List<Node> getRootNodes() {
        return getNodes().stream().filter(n->this.getVertexByNumber(n.getNumber()).getParents().isEmpty()).collect(Collectors.toList());
    }

    public Dag getInterventedDag(Intervention intervention) {
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(getNodes());
        List<Edge> edges = new ArrayList<>();
        edges.addAll(getEdges());


        for(Node target : intervention.getInterventionTargets()){
            Vertex t = getVertexByNumber(target.getNumber());
            for(Node parent : t.getParents()){
                Edge found = findEdge(t.getNode(), parent);
                if(found != null){
                    edges.remove(found);
                }
            }
        }
        return new Dag(nodes, edges);
    }
}
