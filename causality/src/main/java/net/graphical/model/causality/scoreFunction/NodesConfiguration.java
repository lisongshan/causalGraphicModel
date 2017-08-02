package net.graphical.model.causality.scoreFunction;

import net.graphical.model.causality.graph.model.Node;

import java.util.*;

/**
 * Created by sli on 11/10/15.
 */
public class NodesConfiguration {

    public static List<NodesConfiguration> generateNodesConfiguration(Set<Node> nodes){
        List<NodesConfiguration> nodesConfigurations = new ArrayList<>();

        if(nodes.isEmpty()){
            nodesConfigurations.add(new NodesConfiguration());
            return nodesConfigurations;
        }
        for(Node node : nodes){
            nodesConfigurations = expand(node, nodesConfigurations);
        }

        return nodesConfigurations;
    }


    private static List<NodesConfiguration> expand(Node node, List<NodesConfiguration> configs) {

        List<NodesConfiguration> expandedConfigs  = new ArrayList<>();
        for(String level : node.getLevels()){
            if(configs.isEmpty()){
                expandedConfigs.add(new NodesConfiguration(node, level));
            }
            else {
                for (NodesConfiguration config : configs) {
                    expandedConfigs.add(new NodesConfiguration(config, node, level));
                }
            }

        }
        return expandedConfigs;
    }


    private Map<Node, String> nodeToLevelMap = new HashMap<>();

    public NodesConfiguration(){

    }

    public NodesConfiguration(Node node, String level){
        addPart(node, level);
    }

    public NodesConfiguration(NodesConfiguration config, Node newNode, String level) {
        for(Node node : config.getNodes()){
            nodeToLevelMap.put(node, config.getLevel(node));
        }
        nodeToLevelMap.put(newNode, level);
    }

    public void addPart(Node node, String level){
        nodeToLevelMap.put(node,level);
    }

    public Set<Node> getNodes(){
        return nodeToLevelMap.keySet();
    }

    public String getLevel(Node node){
        return nodeToLevelMap.get(node);
    }

    @Override
    public int hashCode(){
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object that){
        return toString().equals(that.toString());
    }


    @Override
    public String toString(){
        String val = "";
        for(Node node : getNodes()){
            val += node.getNumber() + ":" + nodeToLevelMap.get(node) + ",";
        }
        return val;
    }

    public boolean isEmpty() {
        return nodeToLevelMap.size() == 0;
    }

}

