package net.graphical.model.causality.graph.model.AdjImpl;

import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by sli on 10/27/15.
 *
 */
public class Vertex {

    private GraphBase graph;
    private Node node;
    private String label = "";
    private boolean isExplored;
    private List<Edge> edges = new ArrayList<>();//all edges involved, for example, both incident and excident.
    private Vertex[] neigborAndChildrenVertexes;


    public Vertex(GraphBase graph, Node node) {
        this.graph = graph;
        this.node = node;
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public List<Node> getParents(){
        return edges.stream().filter(e -> e.getEdgeType().isDirected())
                .map(e -> e.getParent().getNumber() == node.getNumber() ? null : e.getParent())
                .filter(Objects::nonNull).collect(Collectors.toList());

    }


    public List<Vertex> getParentVertexes(){
        return getParents().stream().map(node -> graph.getVertexByNumber(node.getNumber())).collect(Collectors.toList());

    }

    public List<Node> getChildren(){
        return edges.stream().filter(e -> e.getEdgeType().isDirected())
                .map(e -> e.getChild().getNumber() == node.getNumber() ? null : e.getChild())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }


    public List<Vertex> getChildVertexes(){
        return getChildren().stream().map(node -> graph.getVertexByNumber(node.getNumber()))
                .collect(Collectors.toList());

    }

    public List<Node> getNeighbors(){
        return edges.stream().filter(e-> !e.getEdgeType().isDirected())
                .map(e -> e.getSecondNode().getNumber() == node.getNumber() ? e.getFirstNode() : e.getSecondNode())
                .collect(Collectors.toList());

    }

    public List<Vertex> getNeighborVertexes(){
        return getNeighbors().stream().map(node -> graph.getVertexByNumber(node.getNumber()))
                .collect(Collectors.toList());

    }


    public List<Node> getAdjs(){
        List<Node> adjs = new ArrayList<>();
        adjs.addAll(getParents());
        adjs.addAll(getChildren());
        adjs.addAll(getNeighbors());

        return adjs;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isExplored() {
        return isExplored;
    }

    public void setIsExplored(boolean isExplored) {
        this.isExplored = isExplored;
    }

    public int getVertexNumber(){
        return node.getNumber();
    }

    public int getVertexArrayIndex(){
        return this.graph.getArrayIndex(node.getNumber());
    }

    public GraphBase getGraph() {
        return graph;
    }

    @Override
    public int hashCode(){
        return getVertexNumber();
    }

    @Override
    public boolean equals(Object that){
        return node.getNumber() == ((Vertex) that).node.getNumber();
    }


    @Override
    public String toString(){
        return node.toString();
    }


    public Node getNode() {
        return node;
    }

    public boolean isAdjacent(Node b) {
        return getAdjs().contains(b);
    }

    public boolean isNeighor(Node b) {
        return getNeighbors().contains(b);
    }


    public List<Vertex> getAdjVertexes() {
        List<Vertex> adjs = new ArrayList<>();
        adjs.addAll(getParentVertexes());
        adjs.addAll(getChildVertexes());
        adjs.addAll(getNeighborVertexes());

        return adjs;
    }

    public int getDegree(){
        return getNeighbors().size();
    }


    public void removeEdge(Edge found) {
        edges.remove(found);
    }

    public List<Vertex> getNeigborAndChildrenVertexes() {

        List<Vertex> neigborAndParentVertexes = new ArrayList<>();
        neigborAndParentVertexes.addAll (getNeighborVertexes());
        neigborAndParentVertexes.addAll(getChildVertexes());
        return neigborAndParentVertexes;

    }
}
