package net.graphical.model.causality.graph.model.intervention;

import net.graphical.model.causality.graph.model.Node;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sli on 10/28/15.
 */
public class Intervention {
    List<Node> interventionTargets;
    private String digest;

    public Intervention(){
        this(Arrays.asList());
    }

    public Intervention(List<Node> interventionTargets) {
        this.interventionTargets = interventionTargets;
        digest = getDigest();
    }

    public List<Node> getInterventionTargets() {
        return interventionTargets;
    }

    private String getDigest(){
        if(interventionTargets == null || interventionTargets.isEmpty()){
            return "";
        }
        return interventionTargets.stream().map(node ->  String.valueOf(node.getNumber())).reduce((t1,t2)->t1 + "_" + t2).get();
    }
    @Override
    public int hashCode(){
        return digest.hashCode();
    }

    @Override
    public boolean equals(Object that){
        return digest.equals (((Intervention) that).digest);
    }


    public boolean isIntervened(Node node) {
        return interventionTargets.contains(node);
    }

    @Override
    public String toString(){
        String val = "targets:";
        for(Node node: interventionTargets){
            val += node.getNumber() + ",";
        }
        return val;
    }
}
