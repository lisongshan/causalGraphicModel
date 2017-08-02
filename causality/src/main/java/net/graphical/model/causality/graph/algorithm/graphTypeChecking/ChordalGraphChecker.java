package net.graphical.model.causality.graph.algorithm.graphTypeChecking;

import net.graphical.model.causality.graph.algorithm.ordering.PeoChecker;
import net.graphical.model.causality.graph.model.AdjImpl.ConnectedUndirectedGraph;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;

import java.util.List;

/**
 * Created by sli on 12/1/15.
 */
public class ChordalGraphChecker {
    private ConnectedUndirectedGraph graph;

    public ChordalGraphChecker(ConnectedUndirectedGraph graph) {
        this.graph = graph;
    }

    public boolean isTrue() throws Exception {

        List<Vertex> vertexOrdering = graph.getLexOrdering();
        PeoChecker peoChecker = new PeoChecker(graph);
        return peoChecker.isTrue(vertexOrdering);
    }
}
