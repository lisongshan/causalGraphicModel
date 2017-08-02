package net.graphical.model.causality;

import net.graphical.model.causality.temporal.TemporalOrderEstimator;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.interventionData.DataSet;

/**
 * Created by sli on 2/3/16.
 */
public interface InputProvider extends TemporalOrderEstimator {

    DataSet getDataSet();
    boolean isKnownRelated(Node node1, Node node2);
}
