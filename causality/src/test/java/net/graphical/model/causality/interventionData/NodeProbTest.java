package net.graphical.model.causality.interventionData;

import net.graphical.model.causality.graph.model.Node;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 3/2/16.
 */
public class NodeProbTest {

    @Test
    public void testNodeProbMultiLevel () throws Exception {

        Node node = new Node(1);
        node.addLevel("KK", "KC", "CK", "CC");
        double prob[] = new double[4];
        prob[0] = 0.4;
        prob[1] = 0.1;
        prob[2] = 0.1;
        prob[3] = 0.4;
        NodeProb nodeProb = new NodeProb(node, prob);
        String level = nodeProb.getLevel(0.45);
        assertEquals("KC", level);

    }
}
