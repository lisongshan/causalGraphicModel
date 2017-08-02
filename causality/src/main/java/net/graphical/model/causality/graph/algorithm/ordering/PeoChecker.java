package net.graphical.model.causality.graph.algorithm.ordering;

import net.graphical.model.causality.graph.model.AdjImpl.ConnectedUndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sli on 11/17/15.
 * Check if a ordering is a perfect elimination ordering for a connectedUndirected Graph
 */
public class PeoChecker {

    private ConnectedUndirectedGraph graph;

    public PeoChecker(ConnectedUndirectedGraph graph) {
        this.graph = graph;
    }

    public boolean isTrue(List<Vertex> ordering){

        List<Pair> test = new ArrayList<>();
        for(int j = ordering.size(); j > 0 ; j--){
            Vertex v = ordering.get(j-1);

            List<Vertex> predecessors = new ArrayList<>();
            Vertex lastPredecessor = getLastPredecessor(v, ordering, predecessors);
            if(!predecessors.isEmpty()){
                for(Vertex w : predecessors){
                    if(w != lastPredecessor){
                        Pair entry = new Pair(lastPredecessor,w);
                        test.add (entry);
                    }
                }
            }

        }
        return isAllAdjacent(test);

    }

    private boolean isAllAdjacent(List<Pair> test) {
        for(Pair entry : test){
            if(!entry.first.getNeighborVertexes().contains(entry.second))
                return false;
        }
        return true;
    }

    private Vertex getLastPredecessor(Vertex currentVertex, List<Vertex> ordering, List<Vertex> predecessors) {
        int order = ordering.indexOf(currentVertex);
        Vertex predecessor = null;
        for(int j = order; j > 0 ; j--) {
            Vertex vj = ordering.get(j-1);

            if(currentVertex.getNeighborVertexes().contains(vj)){
                if(predecessor == null){
                    predecessor = vj;
                }
                predecessors.add (vj);
            }
        }
        return predecessor;
    }



    public static class Pair
    {
        public Vertex first;
        public Vertex second;

        public Pair(Vertex first, Vertex second) {
            this.first = first;
            this.second = second;
        }

        private String cb(){
            return String.valueOf(first.getNode().getNumber()) + "_" + String.valueOf(second.getNode().getNumber());
        }

        private String rcb(){
            return String.valueOf(second.getNode().getNumber()) + "_" + String.valueOf(first.getNode().getNumber());
        }

        @Override
        public int hashCode(){
            return first.getNode().getNumber() * second.getNode().getNumber();
        }

        @Override
        public boolean equals(Object other){

            Pair that = (Pair) other;
            return cb().equals(that.cb()) || cb().equals(((Pair) other).rcb());
        }
    }
}
