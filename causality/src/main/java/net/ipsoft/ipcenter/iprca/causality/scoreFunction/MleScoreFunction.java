package net.ipsoft.ipcenter.iprca.causality.scoreFunction;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;

import java.util.List;

/**
 * Created by sli on 11/10/15.
 */
public class MleScoreFunction extends LikelihoodScoreFunction {

    public MleScoreFunction(DataSet dataSet) {

        super(dataSet);
    }

    @Override
    public double getLocalScore(Node node, List<Node> parentNodes) throws Exception {

        //Switch to column nodes
        double score = 0;
        DataSet dataSetInvAccounted = dataSet.accountforInvention(dataSet.getColumnNode(node.getNumber()));
        if(dataSetInvAccounted.getDataSize() == 0){
            return 0;//TODO is it right?
        }
        score = getLocalLikilyhoodScore(dataSet.getColumnNode(node.getNumber()), parentNodes, dataSetInvAccounted);

        return score;

    }



   
}
