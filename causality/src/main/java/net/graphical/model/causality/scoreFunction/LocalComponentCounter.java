package net.graphical.model.causality.scoreFunction;

import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.interventionData.DataSet;

import java.util.List;

/**
 * Created by sli on 11/10/15.
 */
public class LocalComponentCounter {
    private LocalComponent localComponent;
    private DataSet dataSet;

    private int Ni[][];
    private int ni[];

    public LocalComponentCounter(Node node, List<Node> parentNodes, DataSet dataSet) throws Exception {
        localComponent = new LocalComponent(node, parentNodes);

        this.dataSet = dataSet;

        generateCounts();
    }

    public int getNijk(int j, int k){
        return Ni[j][k];
    }

    public int getNij(int j){
        return ni[j];
    }
    private void generateCounts() {

        Ni = new int[localComponent.q_i()][localComponent.r_i()];
        ni = new int[localComponent.q_i()];
        for(int j = 0; j < localComponent.q_i(); j++) {
            for (int k = 0; k < localComponent.r_i(); k++) {
                NodesConfiguration parentsConfig = localComponent.getNodesConfigurations().get(j);
                NodesConfiguration combineConfig = new NodesConfiguration(parentsConfig, localComponent.getNode(),
                        localComponent.getNode().getLevels().get(k));
                int combinedConfigCount = dataSet.getCount(combineConfig);
                Ni[j][k] = combinedConfigCount;
                ni[j] += combinedConfigCount;
            }
        }
    }

    public LocalComponent getLocalComponent() {
        return localComponent;
    }
}
