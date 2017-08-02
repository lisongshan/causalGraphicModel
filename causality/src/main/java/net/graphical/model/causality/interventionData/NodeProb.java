package net.graphical.model.causality.interventionData;

import net.graphical.model.causality.graph.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sli on 12/4/15.
 */
public class NodeProb {

    private Node node;
    private double[] prob;


    public NodeProb(Node node, double[] prob) {
       this.node = node;
        this.prob = prob;
    }

    public NodeProb(Node node) {
        this.node = node;
    }

    public double[] getProb() {
        return prob;
    }

    public String getLevel(double accumulatedProb) {
        List<Double> accProb = getAccumulatedProb();
        for(int i = 0; i < prob.length; i++){
            if(accProb.get(i) > accumulatedProb){
                return node.getLevels().get(i);
            }
        }

        return node.getLevels().get(prob.length - 1);
    }

    private List<Double> getAccumulatedProb(){
        List<Double> accProb = new ArrayList<>();
        for(int i = 0; i < prob.length; i++){
            double p = 0;
            for(int j = 0; j <= i; j++){
                p += prob[j];
            }
            accProb.add(p);
        }

        return accProb;
    }

    public void setProb(double[] prob) {
        this.prob = prob;
    }

    public Node getNode() {
        return node;
    }

    public boolean isSumToOne() {
        double sum = 0.0;
        for(int i = 0; i < prob.length; i++){
            sum += prob[i];
        }
        return sum < 1.0000001 && sum > 0.999999;
    }

    @Override
    public String toString() {
        return "NodeProb{" +
                "node=" + node +
                ", prob=" + Arrays.toString(prob) +
                '}';
    }
}
