package net.ipsoft.ipcenter.iprca.causality;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;
import net.ipsoft.ipcenter.iprca.causality.temporal.TemporalOrderEstimator;

/**
 * Created by sli on 2/3/16.
 */
public interface InputProvider extends TemporalOrderEstimator {

    DataSet getDataSet();
    boolean isKnownRelated(Node node1, Node node2);
}
