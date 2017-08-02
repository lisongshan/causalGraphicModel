package net.graphical.model.causality.graph.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sli on 10/28/15.
 */
public class Node implements Comparable{
    private int number;//node has to be numbered 1,2...n
    private String description;
    private List<String> levels = new ArrayList<>();

    public Node(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void addLevel(String... levels){

        for(int i = 0 ; i < levels.length; i++){
            this.levels.add(levels[i]);
        }

    }

    public int getNumberOfLevels(){
        return levels.size();
    }


    public List<String> getLevels() {
        return levels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return String.valueOf(number);
    }

    @Override
    public int hashCode(){
        return number;
    }

    @Override
    public boolean equals(Object that){
        return number  == ((Node) that).getNumber();
    }

    @Override
    public int compareTo(Object o) {
        return getNumber() - ((Node) o).getNumber();
    }
}
