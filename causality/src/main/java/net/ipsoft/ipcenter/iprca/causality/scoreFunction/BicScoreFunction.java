package net.ipsoft.ipcenter.iprca.causality.scoreFunction;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;

import java.util.List;

/**
 * Created by sli on 11/10/15.
 */
public class BicScoreFunction extends LikelihoodScoreFunction {

    public BicScoreFunction(DataSet dataSet) {

        super(dataSet);
    }

    @Override
    public double getLocalScore(Node node, List<Node> parentNodes) throws Exception {

        LocalComponent localComponent = new LocalComponent(node, parentNodes);
        double score = 0;
        DataSet dataSetInvAccounted = dataSet.accountforInvention(dataSet.getColumnNode(node.getNumber()));
        if(dataSetInvAccounted.getDataSize() == 0){
            return 0;//TODO is it right?
        }
        score = getLocalLikilyhoodScore(dataSet.getColumnNode(node.getNumber()), parentNodes, dataSetInvAccounted);

        //TODO: deliberate on which size to use:
        score = score - 0.5*Math.log(dataSetInvAccounted.getDataSize()) * localComponent.q_i() * (node.getNumberOfLevels() -1);
        return score;

    }




}
