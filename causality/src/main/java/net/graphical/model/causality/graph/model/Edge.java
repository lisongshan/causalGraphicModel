package net.graphical.model.causality.graph.model;


import java.util.List;

/**
 * Created by sli on 10/27/15.
 *
 * * public enum EdgeType {
 UNDIRECTED, firstNode - secondNode
 DIRECTED_PLUS, firstNode -> secondNode
 DIRECTED_MINUS, firstNode <- secondNode

 */
public class Edge {
    private Node firstNode;
    private Node secondNode;
    private EdgeType edgeType;
    private boolean isProtected;

    public Edge(Node firstNode, Node secondNode, EdgeType edgeType) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.edgeType = edgeType;
    }

    public Edge(int firstNodeNumber, int secondNodeNumber, EdgeType edgeType) {
        this(new Node(firstNodeNumber), new Node(secondNodeNumber), edgeType);
    }

    public Node getParent(){

        if(edgeType == EdgeType.DIRECTED_MINUS){
            return secondNode ;
        }
        else if(edgeType == EdgeType.DIRECTED_PLUS){
            return firstNode;
        }
        else{
            return null;
        }

    }


    public Node getChild(){

        if(edgeType == EdgeType.DIRECTED_MINUS){
            return firstNode ;
        }
        else if(edgeType == EdgeType.DIRECTED_PLUS){
            return secondNode;
        }
        else{
            return null;
        }

    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public boolean hasNode(Node node){
        return firstNode.getNumber() == node.getNumber() || secondNode.getNumber() == node.getNumber();
    }


    public Node getSourceNode(){
        if(getEdgeType().isDirectedPlus()){
            return getFirstNode();
        }
        else if (getEdgeType().isDirectedMinus()) {
            return getSecondNode();
        }
        return null;
    }

    public Node getTargetNode(){
        if(getEdgeType().isDirectedPlus()){
            return getSecondNode();
        }
        else if (getEdgeType().isDirectedMinus()) {
            return getFirstNode();
        }
        return null;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(Node secondNode) {
        this.secondNode = secondNode;
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(EdgeType edgeType) {
        this.edgeType = edgeType;
    }

    public boolean isDirected() {
        return edgeType.isDirected();
    }

    @Override
    public String toString(){
        return "(" + String.valueOf(firstNode.getNumber())
                + "," + String.valueOf(secondNode.getNumber())
                +") " + edgeType.toString();
    }

    @Override
    public int hashCode(){
        return firstNode.getNumber() * secondNode.getNumber();
    }

    @Override
    public boolean equals(Object that){
        Edge thatEdge = (Edge) that;

        if(edgeType.isDirected() && thatEdge.isDirected()){
            if(this.edgeType.opposite() == thatEdge.edgeType){
                return this.firstNode.equals(thatEdge.secondNode) && this.secondNode.equals(thatEdge.firstNode);
            }
            else
            {
                return this.firstNode.equals(thatEdge.firstNode) && this.secondNode.equals(thatEdge.secondNode);
            }
        }

        if(!edgeType.isDirected() && !thatEdge.isDirected()){
            return this.firstNode.equals(thatEdge.firstNode) && this.secondNode.equals(thatEdge.secondNode)
                    || this.firstNode.equals(thatEdge.secondNode) && this.secondNode.equals(thatEdge.firstNode);

        }

       return false;
    }



    public boolean hasNodeIn(List<Node> cliqueC) {
        return cliqueC.contains(firstNode) || cliqueC.contains(secondNode);
    }

    public EdgeType getDirection(Node firstNode) {

        if(firstNode.getNumber() == this.firstNode.getNumber())
            return edgeType;
        else if(firstNode.getNumber() == this.secondNode.getNumber())
            return edgeType.opposite();
        else
            return null;
    }

    public Edge copy() {
        return new Edge(firstNode, secondNode, edgeType);
    }
}
