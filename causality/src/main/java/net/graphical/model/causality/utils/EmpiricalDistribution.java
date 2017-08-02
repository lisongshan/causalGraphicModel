package net.graphical.model.causality.utils;

import net.graphical.model.causality.graph.model.AdjImpl.Dag;
import net.graphical.model.causality.model.ConditionalDistribution;
import net.graphical.model.causality.model.Pair;
import net.graphical.model.causality.scoreFunction.NodesConfiguration;
import net.graphical.model.causality.graph.model.AdjImpl.Vertex;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;
import net.graphical.model.causality.interventionData.DataSet;
import net.graphical.model.causality.interventionData.NodeProb;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by sli on 3/2/16.
 */
public class EmpiricalDistribution {

    private DataSet dataSet;
    private ConditionalDistribution cpd;
    private Dag dag;

    public EmpiricalDistribution(DataSet dataSet, ConditionalDistribution cpd, Dag dag) {
        this.dataSet = dataSet;
        this.cpd = cpd;
        this.dag = dag;
    }

    public void checkDataSetAgainstCpd(List<Node> nodesToCheck) throws Exception {
        for(Intervention intervention : dataSet.getInterventionSet()){
            Dag interventedDag = dag.getInterventedDag(intervention);

            List<Node> ordering = interventedDag.topologicalOrdering();

            for(Node node : ordering) {
                if (nodesToCheck.contains(node)) {
                    if (!intervention.getInterventionTargets().contains(node)) {
                        Vertex vertex = interventedDag.getVertexByNumber(node.getNumber());

                        DataSet interventionAccountedDataSet = dataSet.accountforInvention(node);

                        List<NodesConfiguration> parentNodesConfigs = NodesConfiguration.generateNodesConfiguration(new TreeSet(vertex.getParents()));
                        for (NodesConfiguration parentNodesConfig : parentNodesConfigs) {
                            NodeProb nodeProb = cpd.getProb(node, parentNodesConfig);
                            double p[] = new double[node.getLevels().size()];
                            int i = 0;
                            int totalCount = 0;
                            for (String level : node.getLevels()) {
                                NodesConfiguration nodesConfiguration = new NodesConfiguration(node, level);
                                Pair<Integer, Double> resultPair = interventionAccountedDataSet.getEmpiricalProb(nodesConfiguration, parentNodesConfig);
                                p[i++] = resultPair.getSecond();
                                totalCount = resultPair.getFirst();
                            }

                            double diff = 0;

                            for (int index = 0; index < p.length; index++) {
                                diff += (p[index] - nodeProb.getProb()[index]) * (p[index] - nodeProb.getProb()[index]);
                            }

                            if (diff > 0.01) {
                                //throw new Exception("EmpiricalDistribution is not within range of specified");
                            }
                        }

                    }
                }
            }

        }
    }


}
