package net.ipsoft.ipcenter.iprca.causality.graph.algorithm.graphTypeChecking;

import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ChainGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.ConnectedUndirectedGraph;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Dag;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Edge;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.InterventionFamily;

import java.util.List;

/**
 * Created by sli on 10/27/15.
 */
public class EssentialGraphChecker {
    private ChainGraph graph;
    private InterventionFamily interventionFamily;

    public EssentialGraphChecker(ChainGraph graph, InterventionFamily interventionFamily) {
        this.graph = graph;
        this.interventionFamily = interventionFamily;
    }

    public boolean isTrue() throws Exception {

        if(!graph.isPartialEssentential()){
            return false;
        }

        List<ConnectedUndirectedGraph> chainComponents = graph.getChainComponents();

        for(ConnectedUndirectedGraph cug : chainComponents){
            ChordalGraphChecker cgc = new ChordalGraphChecker(cug);
            if(!cgc.isTrue()){
                return false;
            }
        }

        for(Edge edge : graph.getEdges()){
            if(!isProtected(edge)){
                return false;
            }
        }

        return true;
    }


    public Dag getDagExtension(){
        return null;//todo implement dag extension. if no dag extension, then the graph is not an essential graph (complete partial acyclic graph).
    }
    private boolean isProtected(Edge edge){

        if(interventionFamily != null) {
            boolean protecteD = interventionFamily.isProtected(edge);
            if (protecteD) {
                return true;
            }
        }

        return graph.isProtected(edge);
    }
}
