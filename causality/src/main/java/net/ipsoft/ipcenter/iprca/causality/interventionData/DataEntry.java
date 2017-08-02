package net.ipsoft.ipcenter.iprca.causality.interventionData;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.Intervention;
import net.ipsoft.ipcenter.iprca.causality.scoreFunction.NodesConfiguration;

import java.util.Arrays;

/**
 * Created by sli on 10/28/15.
 */
public class DataEntry {
    private Intervention intervention;
    private String[] dataRow;

    public DataEntry(int numberOfColumns) {
        this(new Intervention(Arrays.asList()), numberOfColumns);
    }

    public DataEntry(Intervention intervention, int numberOfColumns) {
        dataRow = new String[numberOfColumns];
        this.intervention = intervention;
    }

    public void addData(int columnNumber, String data){
        dataRow[columnNumber - 1] = data;
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public Intervention getIntervention() {
        return intervention;
    }

    public String getData (int columnIndex) {
        return dataRow[columnIndex-1];
    }

    public boolean containsConfiguration(NodesConfiguration configuration) {
        for(Node node : configuration.getNodes()){
            if(!configuration.getLevel(node).equals(dataRow[node.getNumber() - 1] )){
                return false;
            }
        }
        return true;
    }

    public int getNumberOfColumn(){
        return dataRow.length;
    }
    @Override
    public String toString(){
        String val = intervention.toString() + ":";
        for(int i = 0; i < dataRow.length; i++){
            val += dataRow[i] + ",";
        }
        return val;
    }
}
