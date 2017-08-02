package net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl;


import net.ipsoft.ipcenter.iprca.causality.graph.algorithm.transformer.EssentialGraphGenerator;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Edge;
import net.ipsoft.ipcenter.iprca.causality.graph.model.EdgeType;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.InterventionFamily;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sli on 10/27/15.
 */
public class ChainGraph extends GraphBase {


    public ChainGraph(List<Node> nodes, List<Edge> edges) {
        super(nodes, edges);

    }

    public ChainGraph deepCopy() {
        //deep clone edges only. Nodes are immutable
        List<Edge> newEdges = new ArrayList<>();
        for(Edge edge : this.edges){
            newEdges.add(edge.copy());
        }
        return new ChainGraph(getNodes(), newEdges);

    }


    @Override
    public ChainGraph subtract(List<Node> cliqueC) {
        GraphBase gb = super.subtract(cliqueC);
        return new ChainGraph(gb.getNodes(), gb.getEdges());
    }


    public boolean isProtected(Edge edge) {

        if(!edge.getEdgeType().isDirected()){
            return true;
        }

        return isPatternA(edge) || isPatternB(edge) || isPatternC(edge) || isPatternD(edge);
    }

    /**
     * @param edge
     * pattern A: (a,b,c) with a->b,c->a
     * @return
     */
    private boolean isPatternA(Edge edge){

        Node a = edge.getSourceNode();
        Node b = edge.getTargetNode();

        if(a == null || b == null){
            return false;
        }

        for(Vertex parent : getVertexByNumber(a.getNumber()).getParentVertexes()){
            if(!parent.isAdjacent(b)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param edge
     * pattern A: (a,b,c) with a->b,c->b
     * @return
     */
    private boolean isPatternB(Edge edge){

        Node a = edge.getSourceNode();
        Node b = edge.getTargetNode();

        if(a == null || b == null){
            return false;
        }

        for(Vertex parent : getVertexByNumber(b.getNumber()).getParentVertexes()){
            if(parent != getVertexByNumber(a.getNumber()) && !parent.isAdjacent(a)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param edge
     * pattern C: (a,b,c) with a->b,a->c, c->b
     * @return
     */
    private boolean isPatternC(Edge edge){

        Node a = edge.getSourceNode();
        Node b = edge.getTargetNode();

        if(a == null || b == null){
            return false;
        }

        for(Vertex parent : getVertexByNumber(b.getNumber()).getParentVertexes()){
            if(getVertexByNumber(a.getNumber()).getChildren().contains(parent.getNode())){
                return true;
            }
        }
        return false;
    }

    /**
     * @param edge
     * pattern C: (a,b,c1, c2) with a->b,a-c1, a-c2, c1->b, c2->b
     * @return
     */
    private boolean isPatternD(Edge edge){

        Node a = edge.getSourceNode();
        Node b = edge.getTargetNode();

        if(a == null || b == null){
            return false;
        }

        int i = 0;
        for(Vertex neigbor : getVertexByNumber(a.getNumber()).getNeighborVertexes()){
            if(getVertexByNumber(b.getNumber()).getParents().contains(neigbor.getNode())){
                i++;
                if(i >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPartialEssentential(){
        for(int i = 0; i < getNodes().size(); i++){
            for(int j = i+1; j < getNodes().size(); j++){
                if(hasAntiPartialEssentialPattern(vertexes[i], vertexes[j])){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * AntiPartialEssentialPattern a->b-c
     * @return
     */
    private boolean hasAntiPartialEssentialPattern(Vertex a, Vertex b) {
        if(a.getChildren().contains(b.getNode())){
            for(Vertex c : b.getNeighborVertexes()){
                if(!c.getAdjs().contains(a.getNode())){
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isReachable(Vertex a, Vertex b) {
        this.exploreReset();
        return DFS_reachablilitDetect(a,b);
    }


    public boolean isReachable(Node a, Node b) {
        Vertex aa = getVertexByNumber(a.getNumber());
        Vertex bb = getVertexByNumber(b.getNumber());
        return isReachable(aa, bb);
    }

    private boolean DFS_reachablilitDetect(Vertex a, Vertex b){
        a.setIsExplored(true);
        for(Vertex vertex : a.getNeigborAndChildrenVertexes()){
            if(vertex.getNode().getNumber() == b.getNode().getNumber()){
                return true;
            }
            if(!vertex.isExplored()){
                boolean reached = DFS_reachablilitDetect(vertex, b);
                if(reached) return true;
            }
        }
        return false;
    }

    public ConnectedUndirectedGraph getChainComponent(Node v) {
        for(ConnectedUndirectedGraph cug : getChainComponents()){
            if(cug.getNodes().contains(v))
                return cug;
        }
        return null;
    }




    public ChainGraph makeEssential(InterventionFamily interventionFamily) {
        EssentialGraphGenerator generator = new EssentialGraphGenerator(this);
        generator.toEssentialGraph(interventionFamily);
        return this;
    }




    public void orderEdge(List<Node> ordering) {
        int size = ordering.size();
        for(int i = 0; i < size; i++){
            for(int j = i+1; j < size; j++){
                Edge edge = findEdge(ordering.get(i), ordering.get(j));
                if(edge != null) {
                    edge.setFirstNode(ordering.get(i));
                    edge.setSecondNode(ordering.get(j));
                    edge.setEdgeType(EdgeType.DIRECTED_PLUS);
                }
            }
        }
    }
}
