package net.ipsoft.ipcenter.iprca.causality.scoreFunction;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sli on 11/10/15.
 */
public class LocalComponent {
    private Node node;
    private List<Node> parentNodes;
    private List<NodesConfiguration> nodesConfigurations = new ArrayList<>();

    public LocalComponent(Node node, List<Node> parentNodes) throws Exception {
        this.node = node;
        this.parentNodes = parentNodes;


        for(Node parent : parentNodes){
            if(parent.getLevels().size() == 0)
                throw new Exception("node has no levels");
        }
        doGetParentConfigurations();
    }



    public List<NodesConfiguration> getNodesConfigurations(){
        return nodesConfigurations;
    }


    private void doGetParentConfigurations(){

        if(parentNodes.isEmpty()){
            nodesConfigurations.add(new NodesConfiguration());
            return;
        }
        for(Node node : parentNodes){
            nodesConfigurations = expand(node, nodesConfigurations);
        }
    }

    public int q_i() {
        return getNodesConfigurations().size();
    }

    public int r_i(){
        return node.getLevels().size();
    }


    private List<NodesConfiguration> expand(Node node, List<NodesConfiguration> configs) {

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


    public Node getNode() {
        return node;
    }
}
