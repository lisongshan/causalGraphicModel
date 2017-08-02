package net.graphical.model.causality.graph.model.AdjImpl;


import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.algorithm.ordering.LexBfs;
import net.graphical.model.causality.graph.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sli on 10/27/15.
 */
public class UndirectedGraph extends ChainGraph {


    public UndirectedGraph(List<Node> nodes, List<Edge> edges) {
        super(nodes, edges);

    }

    public List<Vertex> getLexOrdering() throws Exception {
        LexBfs lexBfs = new LexBfs(this);
        return lexBfs.lexBfsOrdering();
    }

    public List<Vertex> getLexOrdering(List<Vertex> initialSequence) throws Exception {
        LexBfs lexBfs = new LexBfs(this, new ArrayList<>(initialSequence));
        return lexBfs.lexBfsOrdering();
    }

    public List<ConnectedUndirectedGraph> getConnectedComponents(){

        List<ConnectedUndirectedGraph> components = new ArrayList<>();
        for(Vertex v : getVertexes()){
            if(!v.isExplored()) {

                List<Node> nodes = new ArrayList<>();
                DFS(v, nodes);
                List<Edge> edges = getInducedEdges(nodes);
                components.add(new ConnectedUndirectedGraph(nodes, edges));
            }
        }
        return components;
    }

    private List<Edge> getInducedEdges(List<Node> nodes) {
        List<Edge> edges = new ArrayList<>();
        for(Edge edge : getEdges()){
            if(nodes.contains(edge.getFirstNode()) & nodes.contains(edge.getSecondNode())){
                edges.add(edge);
            }
        }
        return edges;
    }


    private void DFS(Vertex vertex, List<Node> resultNodes) {

        vertex.setIsExplored(true);
        resultNodes.add(vertex.getNode());

        for(Vertex u : vertex.getNeighborVertexes()){
            if(!u.isExplored()) {
                DFS(u, resultNodes);
            }
        }
    }
}
