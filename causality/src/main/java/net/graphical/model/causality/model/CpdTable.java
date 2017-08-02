package net.graphical.model.causality.model;

import net.graphical.model.causality.scoreFunction.NodesConfiguration;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.interventionData.NodeProb;


import java.util.*;

/**
 * Created by sli on 2/11/16.
 */
public class CpdTable {

    public String getNodedLevel(Node node, String level){
        return node.getNumber() + "-" + level;
    }
    private Map<Pair<String, String>, Double> cpdLookup = new TreeMap<>();

    public CpdTable(){

    }

    public CpdTable(List<Entry> cpdEntries)
    {
        for(Entry entry : cpdEntries){
            Pair<String, String> key = new Pair<>(entry.getNodeLevelProb().getFirst(), entry.getParentConditions());
            cpdLookup.put(key, entry.getNodeLevelProb().getSecond());
        }
    }

    public void add(NodesConfiguration parentsConfiguration, NodeProb nodeProb) {

        Node node = nodeProb.getNode();
        for(int i = 0; i < node.getLevels().size(); i++){
            String nodedLevel = getNodedLevel(node, node.getLevels().get(i));
            double prob = nodeProb.getProb()[i];
            String parentConditions = getParentConditionsAsString(parentsConfiguration);
            cpdLookup.put(new Pair<>(nodedLevel, parentConditions), prob);
        }

    }



    public NodeProb getTimedNodeProb(NodesConfiguration parentsConfiguration, Node node) {

        try{
            NodeProb nodeProb = new NodeProb(node);
            double[] prob = new double[node.getLevels().size()];
            for (int i = 0; i < node.getLevels().size(); i++) {
                String nodeLevel = getNodedLevel(node, node.getLevels().get(i));

                String parentConditions = getParentConditionsAsString(parentsConfiguration);
                Double d  = getProb(nodeLevel, parentConditions);
                if(d == null){
                    return null;
                }

                prob[i] = d;

            }

            nodeProb.setProb(prob);

            return nodeProb;
        }
        catch (Exception e){
            return null;
        }
    }


    public Map<Pair<String, String>, Double> getCpdLookup() {
        return cpdLookup;
    }


    public void addToLookupMap(Pair<String, String> nodeParentConfig, double prob){
        cpdLookup.put(nodeParentConfig, prob);
    }

    private Double getProb(String nodeLevel, String parentConditions) {

        nodeLevel = nodeLevel.replace("OK", "K");
        String nodeLevel_ = nodeLevel.replace("CRITICAL", "C");
        parentConditions = parentConditions.replace("OK", "K");
        String parentConditions_ = parentConditions.replace("CRITICAL", "C");

        return cpdLookup.get(new Pair<> (nodeLevel_, parentConditions_));
    }


    private String getParentConditionsAsString(NodesConfiguration parentsConfiguration) {
        String parentConditions = "";

        if(!parentsConfiguration.isEmpty()) {
            parentConditions = parentsConfiguration.getNodes().stream().sorted()
                    .map(n1 -> n1.getNumber() + "-" + parentsConfiguration.getLevel(n1))
                    .reduce((s1, s2) -> s1 + "," + s2).get();
        }

        return parentConditions;
    }




    public static class Entry{
        private Pair<String, Double> nodeLevelProb;//in the form <1-KK, 0.4>
        private String parentConditions;//in the form of 1-KK,2-KC,5-CK; number in increase order.

        public Entry(String nodeLeve, double prob, String parentConditions) {
            this.nodeLevelProb = new Pair<String, Double> (nodeLeve, prob);
            this.parentConditions = parentConditions;
        }

        public Pair<String, Double> getNodeLevelProb() {
            return nodeLevelProb;
        }

        public String getParentConditions() {
            return parentConditions;
        }
    }
}
