package net.graphical.model.causality.graph.algorithm.transformer;

import net.graphical.model.causality.graph.model.AdjImpl.ConnectedUndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.graph.model.AdjImpl.ChainGraph;
import net.graphical.model.causality.graph.model.Node;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by sli on 10/28/15.
 *
 * Algorithm 1, Reference REF_HB
 */
public class DagGenerator {

   private ChainGraph essentialGraph;

    public DagGenerator(ChainGraph essentialGraph) {
        this.essentialGraph =  essentialGraph;
    }

    public Dag toDag() throws Exception {
        List<ConnectedUndirectedGraph> components = essentialGraph.getChainComponents();
        for(ConnectedUndirectedGraph component : components){
            List<Node> ordering = component.getLexOrdering().stream().map(v->v.getNode()).collect(Collectors.toList());
            essentialGraph.orderEdge(ordering);
        }

        return new Dag(essentialGraph.getNodes(), essentialGraph.getEdges());
    }

}
