package net.graphical.model.causality.graph.model.intervention;

import net.graphical.model.causality.graph.model.Edge;

import java.util.List;

/**
 * Created by sli on 10/28/15.
 */
public class InterventionFamily {
    List<Intervention> interventionList;

    public InterventionFamily(List<Intervention> interventionList) {
        this.interventionList = interventionList;
    }

    public List<Intervention> getInterventionList() {
        return interventionList;
    }

    public boolean isProtected(Edge edge) {
        for(Intervention intervention : interventionList){
            int i = 0;
            if(intervention.getInterventionTargets().contains(edge.getSourceNode())){
                i++;
            }

            if(intervention.getInterventionTargets().contains(edge.getTargetNode())){
                i++;
            }

            if( i == 1){
                return true;
            }

        }
        return false;
    }
}
