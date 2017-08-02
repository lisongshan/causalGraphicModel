package net.ipsoft.ipcenter.iprca.causality.interventionData.dataGenerator;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.interventionData.NodeProb;
import net.ipsoft.ipcenter.iprca.causality.model.ConditionalDistribution;
import net.ipsoft.ipcenter.iprca.causality.model.CpdTable;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.NodesConfiguration;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sli on 12/4/15.
 */
public class OneCpd extends ConditionalDistribution{

    public void add(NodesConfiguration parentsConfiguration, NodeProb nodeProb){
        cpdTable.add(parentsConfiguration, nodeProb);


    }


    @Override
    protected NodeProb doGetNodeProb(Node node, NodesConfiguration parentsConfiguration) throws Exception {
        double[] prob = new double[node.getLevels().size()];
        SecureRandom rd = new SecureRandom();
        double db = rd.nextDouble();

        if(node.getLevels().size() ==2 ) {
            int ip = (int) ((0.2 + 0.6 * db) * 100);
            prob[0] = ip / 100.0;//assign a probablity between 0.2 to 0.8
            prob[1] = 1 - prob[0];
        }


        if(node.getLevels().size() ==4 ) {
            int ip = (int) ((0.2 + 0.8 * db) * 100);
            prob[0] = ip / 400.0;//assign a probablity between 0.2 to 0.8
            prob[1] = ip / 200.0;
            prob[2] = ip / 400.0;
            prob[3] = 1.0 - prob[0] - prob[1]- prob[2];
        }

        return new NodeProb(node, prob);
    }
}
