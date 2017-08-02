package net.ipsoft.ipcenter.iprca.causality.scoreFunction;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;

import java.util.List;

/**
 * Created by sli on 11/10/15.
 */
public abstract  class LikelihoodScoreFunction extends ScoreFunction {

    public LikelihoodScoreFunction(DataSet dataSet) {
        super(dataSet);
    }



    @Override
    public abstract  double getLocalScore(Node node, List<Node> parentNodes) throws Exception;

    protected double getLocalLikilyhoodScore(Node node, List<Node> parentNodes, DataSet dataSet) throws Exception {

        LocalComponentCounter counter = new LocalComponentCounter(node, parentNodes, dataSet);
        LocalComponent localComponent = counter.getLocalComponent();

        int q_i = localComponent.q_i();
        int r_i = localComponent.r_i();

        double score = 0;
        for(int j = 0; j < q_i; j++) {
            for (int k = 0; k < r_i; k++) {
                int nijk = counter.getNijk(j,k);
                int nij = counter.getNij(j);
                score += logScore(nijk, nij);
            }
        }

        return score;
    }


    private static double NEGATIVE_SMALL = Math.log(Math.pow(2.0, -20));
    private static double logScore(int nijk, int nij){
        if( nijk == 0){
            return 0;
        }

        return nijk * Math.log(nijk * 1.0/nij);
    }
}
