package net.graphical.model.causality.graph.model.AdjImpl;


import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sli on 10/27/15.
 */
public class GraphBase {
    protected Vertex[] vertexes;
    protected ArrayList<Edge> edges;

    private Map<Integer, Integer> NodeNumberToArrayIndexMap = new HashMap<>();

    public GraphBase(List<Node> nodes, List<Edge> inputEdges) {
        this.vertexes = new Vertex[nodes.size()];

        int index = 0;
        for(Node node : nodes){
            NodeNumberToArrayIndexMap.put(node.getNumber(), index);
            vertexes[index++] = new Vertex(this, node);
        }
        this.edges = new ArrayList<>() ;

        for(Edge oldEdge : inputEdges){//make sure levels are there
            Edge edge = new Edge(getVertexByNumber(oldEdge.getFirstNode().getNumber()).getNode()
            ,getVertexByNumber(oldEdge.getSecondNode().getNumber()).getNode()
                    , oldEdge.getEdgeType()
                    );

            this.edges.add(edge);
            vertexes[NodeNumberToArrayIndexMap.get(edge.getFirstNode().getNumber())].addEdge(edge);
            vertexes[NodeNumberToArrayIndexMap.get(edge.getSecondNode().getNumber()) ].addEdge(edge);
        }
    }



     public List<Vertex> getVertexes() {
        return Arrays.asList(vertexes);
    }

    public Vertex getVertexByNumber(int nodeNumber){
        return vertexes[NodeNumberToArrayIndexMap.get(nodeNumber)];
    }

    public boolean isDirectedGraph(){
       return  edges.stream().filter(e->!e.getEdgeType().isDirected()).collect(Collectors.toList()).isEmpty();

    }

    public boolean isUnDirectedGraph(){
        return  edges.stream().filter(e->e.getEdgeType().isDirected()).collect(Collectors.toList()).isEmpty();

    }


    public List<Edge> getEdges() {
        return edges;
    }

    public void exploreReset() {

        for (int i = 0; i < vertexes.length; i++){
            vertexes[i].setIsExplored(false);
        }
    }

    public List<Node> getNodes() {
        return Arrays.asList(vertexes).stream().map(v -> v.getNode()).collect(Collectors.toList());
    }

    //follow algorithm presented in REF_UNO
    //Directed Edges are ignored.
    public List<List<Node>> getAllCliques(){
        return getAllCliques(getNodes());
    }

    public List<List<Node>> getAllCliques(List<Node> subNodes){
        SortedSet<Node> CAND_Empty = new TreeSet<Node>() ;//vertexes neigbor to all vertexes of clique K
        CAND_Empty.addAll(subNodes);
        Map<List<Node>, SortedSet<Node>> cliqueToCandKMap = new HashMap<>();
        cliqueToCandKMap.put(Arrays.asList(), CAND_Empty);

        List<List<Node>> result = new ArrayList<>();

        while(!cliqueToCandKMap.isEmpty()){
            List<List<Node>> cliques = new ArrayList<>();

            Map<List<Node>, SortedSet<Node>> newCliqueToCandKMap = new HashMap<>();

            for(List<Node> clique : cliqueToCandKMap.keySet()){
                cliques.add(clique);

                //update newCliqueToCandKMap
                SortedSet<Node> cand = cliqueToCandKMap.get(clique);
                for(Node node : cand){
                    if(node.getNumber() > tail(clique)){
                        List<Node> newClique = new ArrayList<>();
                        newClique.addAll(clique);
                        newClique.add(node);

                        newCliqueToCandKMap.put(newClique, updateCand(cand, node));
                    }
                }

            }
            result.addAll(cliques);

            cliqueToCandKMap = newCliqueToCandKMap;
        }

        return result;
    }

    private SortedSet<Node> updateCand(SortedSet<Node> cand, Node node) {
        Vertex v = getVertexByNumber(node.getNumber());
        Set<Node> neighbors = new TreeSet<>(v.getNeighbors());
        TreeSet<Node> newCand = new TreeSet<>(cand);
        newCand.retainAll(neighbors);

        return newCand;
    }

    private int tail(List<Node> clique) {
        if(clique.isEmpty())
            return -1;

        return clique.stream().map(node -> new Integer(node.getNumber())).max((i1,i2) -> i1 - i2).get().intValue();
    }

    //Bron–Kerbosch algorithm: standard
    //Directed edges are ignored.
    public List<List<Node>> getAllMaxmalCliques_standard(){
        List<List<Node>> result = new ArrayList<>();

        doBronKerbosch_standard(new ArrayList<>(), new TreeSet<>(getNodes()), new TreeSet<>(), result);

        return result;
    }

    //Directed edges are ignored.
    public List<List<Node>> getAllMaximalCliques(List<Node> subNodes){
        List<List<Node>> result = new ArrayList<>();

        doBronKerbosch_standard(new ArrayList<>(), new TreeSet<>(subNodes), new TreeSet<>(), result);

        return result;
    }

    public List<ConnectedUndirectedGraph> getChainComponents(){
        List<Edge> edges = new ArrayList<>();
        for(Edge edge:  getEdges()){
            if(!edge.isDirected()){
                edges.add(edge);
            }
        }
        UndirectedGraph ug =  new UndirectedGraph(getNodes(), edges);
        return ug.getConnectedComponents();

    }


    /**
     * algrothm: REF_CK
     * @param R
     * @param P
     * @param X
     * @param result
     *
     * R is a clique
     * Constraints between R,P,X:
     * P is subset of CAND(R)
     * R and X cannot be neigbors (any)
     *
     * Important: directed edges are ignored.
     */
    public void doBronKerbosch_standard(List<Node> R, TreeSet<Node> P, TreeSet<Node> X, List<List<Node>> result ){
        if(X.isEmpty() && P.isEmpty()){
            List<Node> clique = new ArrayList<>(R);
            result.add (clique);
            R.clear();
        }
        else
        {
            int size = P.size();
            for(int i = 0; i < size; i++){
                Node node = P.first();
                P.remove(node);

                List<Node> newR = new ArrayList<>(R);
                newR.add(node);
                TreeSet newP = new TreeSet(P);
                newP.retainAll(getVertexByNumber(node.getNumber()).getNeighbors());
                TreeSet newX = new TreeSet(X);
                newX.retainAll(getVertexByNumber(node.getNumber()).getNeighbors());
                doBronKerbosch_standard(newR, newP, newX, result);
                X.add(node);
            }
        }
    }

    public List<List<Node>> getAllMaxmalCliques_pivot(){

        List<List<Node>> result = new ArrayList<>();

        doBronKerbosch_pivot(new ArrayList<>(), new TreeSet<>(getNodes()), new TreeSet<>(), result);

        return result;
    }

    //algrothm: REF_CK
    private void doBronKerbosch_pivot(List<Node> R, TreeSet<Node> P, TreeSet<Node> X, List<List<Node>> result ){
        if(X.isEmpty() && P.isEmpty()){
            List<Node> clique = new ArrayList<>(R);
            result.add (clique);
            R.clear();
        }
        else
        {
            Node pivotNode = getPivotNode(P);
            int size = P.size();
            for(int i = 0; i < size; i++){
                if(getVertexByNumber(pivotNode.getNumber()).isNeighor(pivotNode)){
                    continue;
                }

                Node node = P.first();
                P.remove(node);

                List<Node> newR = new ArrayList<>(R);
                newR.add(node);
                TreeSet newP = new TreeSet(P);
                newP.retainAll(getVertexByNumber(node.getNumber()).getNeighbors());
                TreeSet newX = new TreeSet(X);
                newX.retainAll(getVertexByNumber(node.getNumber()).getNeighbors());
                doBronKerbosch_pivot(newR, newP, newX, result);
                X.add(node);
            }
        }
    }

    private Node getPivotNode(TreeSet<Node> p) {

        //just one way of chosing pivot node;
        if(p.isEmpty())
            return null;
        return p.stream().max((n1,n2)-> getVertexByNumber(n1.getNumber()).getDegree() - getVertexByNumber(n2.getNumber()).getDegree()).get();

    }

    public GraphBase subtract(List<Node> cliqueC) {
        List<Node> nodeList = getNodes().stream().filter(n-> !cliqueC.contains(n)).collect(Collectors.toList());

        List<Edge> edgeList = getEdges().stream().filter(e -> !e.hasNodeIn(cliqueC)).collect(Collectors.toList());;

        return new GraphBase(nodeList, edgeList);
    }

    public boolean hasSameEdges(ChainGraph g0) {
        Set<Edge> thisEdgeSet = new HashSet<>(this.getEdges());
        Set<Edge> thatEdgeSet = new HashSet<>(g0.getEdges());

        return thisEdgeSet.equals(thatEdgeSet);
    }

    public void removeEdge(Node v, Node u) {
        Edge found = findEdge(v, u);

        edges.remove(found);

        //update Vertex edge list;
        Vertex vV = getVertexByNumber(v.getNumber());
        vV.removeEdge(found);
        Vertex uV = getVertexByNumber(u.getNumber());
        uV.removeEdge(found);

    }

    public void addDirectedEdge(Node u, Node v) {
        //add u->u, where u, v is already in graph
        Edge newEdge = new Edge(u, v, EdgeType.DIRECTED_PLUS);
        edges.add(newEdge);

        //update Vertex edge list;
        Vertex vV = getVertexByNumber(v.getNumber());
        vV.addEdge(newEdge);
        Vertex uV = getVertexByNumber(u.getNumber());
        uV.addEdge(newEdge);
    }

    public Edge findEdge(Node v, Node u) {
        for(Edge edge : edges){
            if(edge.hasNode(v) && edge.hasNode(u)){
                return edge;
            }
        }
        return null;
    }


    public void turnDirectedEdge(Node v, Node u) {
        removeEdge(v, u);
        addDirectedEdge(u,v);
    }


    public int noOfUndirectEdge() {
        int count = 0;
        for(Edge edge : edges){
            if(!edge.getEdgeType().isDirected()){
                count ++;
            }
        }
        return count;
    }

    public int noOfDirectEdge() {
        int count = 0;
        for(Edge edge : edges){
            if(edge.getEdgeType().isDirected()){
                count ++;
            }
        }
        return count;
    }

    public int getArrayIndex(int number) {
        return NodeNumberToArrayIndexMap.get(number);
    }

    @Override
    public String toString(){
       String str = "number of edges: " + edges.size() + "\n";
        for(Edge edge : edges){
            str += edge.toString() + "\n";
        }
        return str;
    }
}
