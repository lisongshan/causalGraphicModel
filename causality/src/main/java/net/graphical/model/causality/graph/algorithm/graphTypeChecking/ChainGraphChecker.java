package net.graphical.model.causality.graph.algorithm.graphTypeChecking;

import net.graphical.model.causality.graph.model.AdjImpl.ConnectedUndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.GraphBase;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sli on 10/27/15.
 */
public class ChainGraphChecker {
    private GraphBase graph;

    public ChainGraphChecker(GraphBase graph) {
        this.graph = graph;
    }

    public boolean isTrue() throws Exception {

        List<ConnectedUndirectedGraph> chainComponents = graph.getChainComponents();

        int size = chainComponents.size();
        List<Node> nodes = new ArrayList<>();
        for(int i=0; i < size; i++){
            nodes.add(new Node(i));
        }

        List<Edge> edges = new ArrayList<>();
        for(int i = 0; i < size; i++){
            for (int j = i+1; j < size; j++){
                ConnectionStatus connectionType = connectionType(chainComponents.get(i), chainComponents.get(j));
                if(connectionType.isLoop()) {
                    return false;
                }
                else if (connectionType.isConnnected)
                {
                    edges.add(new Edge(new Node(i), new Node(j), connectionType.edgeType)) ;
                }
            }
        }

        DagChecker dagChecker = new DagChecker(new GraphBase(nodes, edges));
        return dagChecker.isTrue();
    }

    private ConnectionStatus connectionType(ConnectedUndirectedGraph cug1, ConnectedUndirectedGraph cug2) {

        EdgeType currentEdgeType = null;
        for(Node node1 : cug1.getNodes() ){
            for (Node node2 : cug2.getNodes()){
                Edge edge = graph.findEdge(node1, node2);
                if(edge == null){
                    continue;
                }

                EdgeType edgeType = edge.getDirection(node1);
                if(currentEdgeType == null){
                    currentEdgeType = edgeType;
                }
                else
                {
                    if(currentEdgeType != edgeType){
                        return new ConnectionStatus(null, true);
                    }
                }
            }
        }

        return currentEdgeType == null ? new ConnectionStatus(null, false) : new ConnectionStatus(currentEdgeType, true);
    }

    public static class ConnectionStatus{
        private EdgeType edgeType;
        private boolean isConnnected;

        public ConnectionStatus(EdgeType edgeType, boolean isConnnected) {
            this.edgeType = edgeType;
            this.isConnnected = isConnnected;
        }

        public EdgeType getEdgeType() {
            return edgeType;
        }


        public boolean isLoop(){
            return isConnnected && edgeType == null;
        }
    }
}
