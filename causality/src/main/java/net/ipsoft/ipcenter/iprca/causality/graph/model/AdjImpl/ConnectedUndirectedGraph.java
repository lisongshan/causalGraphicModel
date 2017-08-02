package net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl;


import net.ipsoft.ipcenter.iprca.causality.graph.model.Edge;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;

import java.util.List;

/**
 * Created by sli on 10/27/15.
 */
public class ConnectedUndirectedGraph extends UndirectedGraph {


    public ConnectedUndirectedGraph(List<Node> nodes, List<Edge> edges) {
        super(nodes, edges);

    }


}
