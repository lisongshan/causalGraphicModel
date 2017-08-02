package net.graphical.model.causality.graph.algorithm.transformer;

import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.Edge;
import net.graphical.model.causality.graph.model.EdgeType;
import net.graphical.model.causality.graph.model.intervention.InterventionFamily;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sli on 10/28/15.
 *
 * Algorithm 1, Reference REF_HB
 */
public class EssentialGraphGenerator {

   private ChainGraph partialEssentialGraph;

    public EssentialGraphGenerator(ChainGraph partialEssentialGraph) {
        this.partialEssentialGraph =  partialEssentialGraph;
    }

    public  ChainGraph toEssentialGraph(){
        return toEssentialGraph(null);
    }

    public  ChainGraph toEssentialGraph(InterventionFamily interventionFamily){
        boolean keepGoing = true;
        while(keepGoing) {
            List<Edge> protectedOrUndireted = new ArrayList<>();
            for (Edge edge : partialEssentialGraph.getEdges()) {
                if (edge.isDirected() && !isProtected (edge, interventionFamily)) {
                    edge.setEdgeType(EdgeType.UNDIRECTED);
                }
                else{
                    protectedOrUndireted.add (edge);
                }
            }

            keepGoing = protectedOrUndireted.size () != partialEssentialGraph.getEdges().size();
        }
        return partialEssentialGraph;
    }

    private boolean isProtected(Edge edge,InterventionFamily interventionFamily ){

        if(interventionFamily != null) {
            boolean protecteD = interventionFamily.isProtected(edge);
            if (protecteD) {
                return true;
            }
        }

        return partialEssentialGraph.isProtected(edge);
    }

}
