package net.ipsoft.ipcenter.iprca.causality.interventionData.dataGenerator;

import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Dag;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Vertex;
import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.Intervention;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataEntry;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;
import net.ipsoft.ipcenter.iprca.causality.interventionData.NodeProb;
import net.ipsoft.ipcenter.iprca.causality.model.ConditionalDistribution;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.NodesConfiguration;
import net.ipsoft.ipcenter.iprca.causality.utils.DataSetStore;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sli on 12/4/15.
 */
public class CausalModel {
    private Dag dag;
    private ConditionalDistribution cpd;

    public CausalModel(Dag dag, ConditionalDistribution cpd) {
        this.dag = dag;
        this.cpd = cpd;
    }

    public Dag getDag() {
        return dag;
    }

    public ConditionalDistribution getCpd() {
        return cpd;
    }

    public  DataSet generateDataSet(int count, List<Intervention> interventions) throws Exception {

        List<DataEntry> dataEntries = new ArrayList<>();
        for(Intervention intervention : interventions){
            List<DataEntry> dataEntrysPartial = generateDataEntries(intervention, count/interventions.size());
            dataEntries.addAll(dataEntrysPartial);
        }
        DataSet ds = new DataSet(dag.getNodes(), dataEntries);
        DataSetStore dataStore = new DataSetStore(new Date().getTime() + "_ds.txt");
        dataStore.save(ds);
        return ds;
    }
    public List<DataEntry> generateDataEntries(Intervention intervention, int noOfEntries) throws Exception {
        int i = 0;
        List<DataEntry> dataEntries = new ArrayList<>();
        while(i < noOfEntries){
            DataEntry de = generateDataEntry(intervention);
            dataEntries.add(de);
            i++;
        }

        return dataEntries;
    }

    private DataEntry generateDataEntry(Intervention intervention) throws Exception {

        Dag interventedDag = dag.getInterventedDag(intervention);

        DataEntry dataEntry = new DataEntry(intervention, dag.getNodes().size());
        List<Node> ordering = interventedDag.topologicalOrdering();
        for(Node node : ordering){
            Vertex vertex = interventedDag.getVertexByNumber(node.getNumber());

            NodeProb nodeProb = null;
            if(intervention.isIntervened(vertex.getNode())){//if intervened, interven uniformally
                double[] prob = new double[node.getLevels().size()];
                for(int i = 0; i < prob.length; i++){
                    prob[i] =  1.0/node.getLevels().size();
                }
                nodeProb = new NodeProb(node, prob);
            }
            else {
                NodesConfiguration parentNodesConfig = getParentsNodeConfig(dataEntry, vertex);
                nodeProb = cpd.getProb(node, parentNodesConfig);
            }
            SecureRandom rd = new SecureRandom();
            double p = rd.nextDouble();
            String level = nodeProb.getLevel(p);
            dataEntry.addData(node.getNumber(), level);

        }
        return dataEntry;
    }

    private NodesConfiguration getParentsNodeConfig(DataEntry dataEntry, Vertex vertex) {
        NodesConfiguration nodesConfiguration = new NodesConfiguration();
        for(Node parent : vertex.getParents()){

            String level = dataEntry.getData(parent.getNumber());
            nodesConfiguration.addPart(parent, level);
        }

        return nodesConfiguration;
    }




}
