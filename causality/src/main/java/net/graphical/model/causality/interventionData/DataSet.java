package net.graphical.model.causality.interventionData;

import net.graphical.model.causality.graph.model.intervention.InterventionFamily;
import net.graphical.model.causality.model.Pair;
import net.graphical.model.causality.scoreFunction.NodesConfiguration;
import net.graphical.model.causality.graph.model.Node;
import net.graphical.model.causality.graph.model.intervention.Intervention;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sli on 10/28/15.
 */
public class DataSet {
    private List<DataEntry> dataEntries;
    private Set<Intervention> interventionSet = new HashSet<>();

    private List<Node> columnNodes;
    public DataSet(List<Node> columnNodes) {
        this.columnNodes = columnNodes;
    }

    public DataSet(List<Node> columnNodes, List<DataEntry> dataEntries) {
        this(columnNodes);
        this.dataEntries = dataEntries;
        for(DataEntry de : dataEntries){
            interventionSet.add(de.getIntervention());
        }
    }


    public List<DataEntry> getDataEntries() {

        return dataEntries;
    }

    public List<Node> getColumnNodes() {
        return columnNodes;
    }

    public Set<Intervention> getInterventionSet() {
        return interventionSet;
    }

    public InterventionFamily getInterventionFamily() {

        return new InterventionFamily(interventionSet.stream().collect(Collectors.toList()));
    }

    public int getCount(NodesConfiguration configuration){
        int count = 0;
        for(DataEntry de : dataEntries){
            if(de.containsConfiguration(configuration)){
                count++;
            }
        }
        return count;
    }

    public Pair<Integer, Double> getEmpiricalProb(NodesConfiguration nodesConfiguration, NodesConfiguration parentsConfiguration){
        int parentsCount = 0;
        int nodesCount = 0;
        for(DataEntry de : dataEntries){
            if(de.containsConfiguration(parentsConfiguration)){
                parentsCount++;
                if(de.containsConfiguration(nodesConfiguration)){
                    nodesCount++;
                }
            }
        }
        return new Pair<> (parentsCount, nodesCount * 1.0/parentsCount);
    }

    public int getDataSize() {
        return dataEntries.size();
    }

    public DataSet accountforInvention(Node node) {

        List<DataEntry> reducedDataEntries = new ArrayList<>();
        for(DataEntry dataEntry : dataEntries){
            if(!dataEntry.getIntervention().isIntervened(node)){
                reducedDataEntries.add(dataEntry);
            }
        }
        return new DataSet(this.columnNodes, reducedDataEntries);
    }



    public int getColumnCount() {
        return columnNodes.size();
    }

    public Node getColumnNode(int columnNumber) {
        return columnNodes.get(columnNumber -1);
    }
}
