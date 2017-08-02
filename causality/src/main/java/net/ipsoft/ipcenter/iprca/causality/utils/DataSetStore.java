package net.ipsoft.ipcenter.iprca.causality.utils;

import net.ipsoft.ipcenter.iprca.causality.graph.model.Node;
import net.ipsoft.ipcenter.iprca.causality.graph.model.intervention.Intervention;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataEntry;
import net.ipsoft.ipcenter.iprca.causality.interventionData.DataSet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sli on 12/4/15.
 */
public class DataSetStore {

    private String fileName;

    public DataSetStore(String fileName) {
        this.fileName = fileName;
    }

    public  DataSet load() throws IOException {

            File file = new File(fileName);


            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = bufferedReader.readLine();
            int columnCount = Integer.valueOf(line);

            List<Node> columnNodes = new ArrayList<>();
            for(int i = 0; i < columnCount; i++){
                line = bufferedReader.readLine();
                String[] strings = line.split(":");
                Node columnNode = new Node(Integer.valueOf(strings[0]));
                String[] levels = strings[1].split(",");

                for(String level : levels){
                    if(!"".equals(level.trim())){
                        columnNode.addLevel(level);
                    }
                }
                columnNodes.add(columnNode);
            }

            List<DataEntry> dataEntries = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split(":");
                String[] targets = strings[1].split(",");


                List<Node> interventionTargets = new ArrayList<>();
                for(String target : targets){
                    if(!"".equals(target.trim())){

                        int ta = Integer.valueOf(target);
                        Node targetNode = columnNodes.get(ta - 1);
                        interventionTargets.add(targetNode);
                    }
                }

                Intervention intervention = new Intervention(interventionTargets);
                DataEntry dataEntry = new DataEntry(intervention, columnCount);
                dataEntries.add(dataEntry);

                String[] dataRow = strings[2].split(",");

                for(int i = 1; i <= dataRow.length; i++){
                    String data = dataRow[i-1];
                    if(!"".equals(data.trim())){
                        dataEntry.addData(i, data);
                    }
                }
            }

            reader.close();

           return new DataSet(columnNodes, dataEntries);



    }


    private String fromDataSet(DataSet dataSet) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dataSet.getColumnCount() + "\n");
        for(Node node : dataSet.getColumnNodes()){
            stringBuilder.append(node.getNumber() +":" + node.getLevels().stream().reduce((l1, l2)-> l1 + "," + l2).get() + "\n");
        }
        for(DataEntry de : dataSet.getDataEntries()){
            stringBuilder.append(de.toString() + "\n");
        }

        return stringBuilder.toString();
    }

    public  void save(DataSet dataSet){
        try {
            File file = new File(fileName);
            OutputStream output = new FileOutputStream(file);
            byte[] contentInBytes = fromDataSet(dataSet).getBytes();

            output.write(contentInBytes);
            output.flush();
            output.close();
        } catch (Exception e) {

        }

    }



}
