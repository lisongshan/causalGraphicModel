package net.graphical.model.causality.utils;

import net.graphical.model.causality.model.CpdTable;
import net.graphical.model.causality.model.Pair;

import java.io.*;
import java.util.Date;


/**
 * Created by sli on 12/4/15.
 */
public class CpdTableStore {

    private String fileName;

    public CpdTableStore() {
        this.fileName = new Date().getTime() + "_cpd.txt";
    }

    public CpdTableStore(String fileName) {
        this.fileName = fileName;
    }

    public CpdTable load() throws IOException {

            File file = new File(fileName);


            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            bufferedReader.readLine(); //skip first line



            CpdTable cpdTable = new CpdTable();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("__");
                double prob = Double.valueOf(strings[1].trim ());

                String [] keys = strings[0].trim ().split("\\|");

                String parentsConfig = keys.length == 1? "" : keys[1].trim();
                Pair<String, String> key = new Pair<String, String>(keys[0].trim (), parentsConfig);
                cpdTable.addToLookupMap(key, prob);

            }

            reader.close();

           return cpdTable;
    }


    private String fromCpdTable(CpdTable cpdTable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cpdTable.getCpdLookup().size() + "\n");
        for(Pair<String, String> key : cpdTable.getCpdLookup().keySet()){
            double value = cpdTable.getCpdLookup().get(key);
            stringBuilder.append(key.toString() + "__" + value + "\n");
        }

        return stringBuilder.toString();
    }

    public  void save(CpdTable cpdTable){
        try {
            File file = new File(fileName);
            OutputStream output = new FileOutputStream(file);
            byte[] contentInBytes = fromCpdTable(cpdTable).getBytes();

            output.write(contentInBytes);
            output.flush();
            output.close();
        } catch (Exception e) {

        }

    }



}
