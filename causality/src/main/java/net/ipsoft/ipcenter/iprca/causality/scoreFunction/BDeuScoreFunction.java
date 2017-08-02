package net.ipsoft.ipcenter.iprca.causality.scoreFunction;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;
import org.apache.commons.math3.special.Gamma;

import java.util.List;

/**
 * Created by sli on 11/10/15.
 */
public class BDeuScoreFunction extends ScoreFunction {

    public BDeuScoreFunction(DataSet dataSet) {

       this(dataSet, 0.001, 10);
    }

    public BDeuScoreFunction(DataSet dataSet, double priorDistBase, int  priorEquSampleSize) {

        super(dataSet);
        this.priorDistBase = priorDistBase;
        this.priorEquSampleSize = priorEquSampleSize;
    }

    private double priorDistBase;
    private int priorEquSampleSize;


    @Override
    public double getLocalScore(Node node, List<Node> parentNodes) throws Exception {

        //todo remove the hacck:
        if(node.getNumber() > 4){
            return 0;
        }


        LocalComponentCounter counter = new LocalComponentCounter(node, parentNodes, dataSet);
        LocalComponent localComponent = counter.getLocalComponent();

        int q_i = localComponent.q_i();
        int r_i = localComponent.r_i();

        double score = (r_i  -1) * q_i * Math.log(priorDistBase);

        for(int j = 0; j < q_i; j++) {

            double nominator = Gamma.logGamma(priorEquSampleSize * 1.0 / q_i);
            double denominator = Gamma.logGamma(priorEquSampleSize * 1.0 / q_i + counter.getNij(j));
            score += nominator - denominator;

            for (int k = 0; k < r_i; k++) {
                double nominatorSub = Gamma.logGamma(priorEquSampleSize * 1.0/q_i/r_i + counter.getNijk(j, k));
                double denoimatorSub = Gamma.logGamma(priorEquSampleSize * 1.0/q_i/r_i);

                score += nominatorSub - denoimatorSub;
            }
        }
        return score;

    }

}
