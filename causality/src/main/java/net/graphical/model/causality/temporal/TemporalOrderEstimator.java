package net.graphical.model.causality.temporal;


/**
 * Created by sli on 2/2/16.
 */
public interface TemporalOrderEstimator {
    double getTypeLevelNode1BeforeNode2Prob(String feature1, String feature2);
    double getTokenLevelNode1BeforeNode2Prob(String feature1, String feature2);


}
