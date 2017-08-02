package net.graphical.model.causality.graph.algorithm.ordering;

import net.graphical.model.causality.graph.model.AdjImpl.UndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sli on 10/27/15.
 *
 * Lexicographic breadth-first search
 * Algorithm 6: Reference REF_HB
 */
public class LexBfs {

    private UndirectedGraph graph;//GraphType.UNDIRECTED
    private ArrayList<Vertex> initialSearchSequence;

    public LexBfs(UndirectedGraph graph) {
        this.graph = graph;
        initialSearchSequence = new ArrayList<> (graph.getVertexes());
    }


    public LexBfs(UndirectedGraph graph, ArrayList<Vertex> initialSearchSequence) {
        this(graph);
        this.initialSearchSequence = initialSearchSequence;
    }

    public ArrayList<Vertex> lexBfsOrdering() throws Exception {

        ArrayList<Vertex> ordering = new ArrayList<>();
        if(!graph.isUnDirectedGraph())
            throw new Exception("LexBfs ordering failed: input graph is not an undirected graph");

        graph.exploreReset();

        Sigma sigma = new Sigma(initialSearchSequence);

        while(!sigma.isEmpty()) {
            Vertex v = sigma.removeFirst();
            ordering.add(v);
            sigma.update(v);
        }
        return ordering;
    }


    public class Sigma{
        SortedMap<String, ArrayList<Vertex>> sortedBuckets = new TreeMap<>();
        LinkedList<ArrayList<Vertex>> linkedSequenceList = new LinkedList<>();

        public Sigma(ArrayList<Vertex> initialSequence){

            linkedSequenceList.add(initialSequence);
            sortedBuckets.put("", initialSequence);
        }

        public void update(Vertex orderedVertex){
            List<Vertex> neigbors = orderedVertex.getNeighborVertexes();

            List<ArrayList<Vertex>> sequenceListClone = linkedSequenceList.stream().collect(Collectors.toList());
            for(ArrayList<Vertex> sequence : sequenceListClone){
                List<Vertex> bucketClone = sequence.stream().collect(Collectors.toList());

                for(Vertex vertex : bucketClone){
                    if(neigbors.contains(vertex) && sequence.size() > 1){
                        sequence.remove(vertex);
                        if(sequence.isEmpty()){
                            sortedBuckets.remove(vertex.getLabel());
                            linkedSequenceList.remove(sequence);
                        }
                        String newLabel = vertex.getLabel() + orderedVertex.getVertexNumber();
                        vertex.setLabel(newLabel);
                        addToBuckets(vertex, sequence);
                    }

                }
            }
        }


        private void addToBuckets(Vertex neigbor, ArrayList<Vertex> sequence) {
            ArrayList<Vertex> bucket = sortedBuckets.get(neigbor.getLabel());
            if(bucket == null){
                bucket = new ArrayList<>();
                sortedBuckets.put (neigbor.getLabel(), bucket);
                int position = linkedSequenceList.indexOf(sequence);
                linkedSequenceList.add (position, bucket);
            }
            bucket.add(neigbor);
        }


        public Vertex removeFirst(){
            ArrayList<Vertex> firstSequence = linkedSequenceList.getFirst();
            Vertex first = firstSequence.remove(0);
            if(firstSequence.isEmpty()){
                linkedSequenceList.remove(firstSequence);
                sortedBuckets.remove(first.getLabel());
            }
            return first;
        }

        public boolean isEmpty(){
            return sortedBuckets.isEmpty();
        }
    }
}
