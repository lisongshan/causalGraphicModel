package net.ipsoft.ipcenter.iprca.causality.graph.algorithm.graphTypeChecking;

import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.GraphBase;
import net.ipsoft.ipcenter.iprca.causality.graph.model.AdjImpl.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by sli on 10/27/15.
 *
 * implementation of Kosaraju's algorithm
 */
public class DagChecker {
    private GraphBase graph;

    public DagChecker(GraphBase graph) {
        this.graph = graph;
        leaders = new Vertex[graph.getVertexes().size()];
    }

    public boolean isTrue(){
        if(!graph.isDirectedGraph())
            return false;

        if(getStrongConnectedComponents().size() == graph.getVertexes().size())
            return true;

        return false;
    }

    public List<List<Vertex>> getStrongConnectedComponents(){

        graph.exploreReset();
        preprocess_loop_dfs();
        List<List<Vertex>> components = new ArrayList<>();

        graph.exploreReset();

        finishNumberNodeMap.values().stream().forEach(i->{
                    Vertex vertex = graph.getVertexByNumber(i.intValue());
                    if (!vertex.isExplored()) {
                        List<Vertex> component = new ArrayList<Vertex>();
                        dfs(vertex, component);
                        components.add(component);
                    }
                }

        );

        return components;
    }

    private void preprocess_loop_dfs(){
        for(Vertex vertex : graph.getVertexes()){
            if(!vertex.isExplored()){
                curLeader = vertex;
                reverse_dfs(vertex);
            }
        }
    }

    private int curFinishNumber = 0;
    private Vertex curLeader = null;
    private Vertex[] leaders;

    private SortedMap<Integer, Integer> finishNumberNodeMap = new TreeMap<>(
            (t1, t2) -> t2 - t1
    );


    private void reverse_dfs(Vertex vertex){
        vertex.setIsExplored(true);
        leaders[vertex.getVertexArrayIndex()] = curLeader;
        for(Vertex v : vertex.getParentVertexes()){
              if(!v.isExplored())
                  reverse_dfs(v);
        }
        curFinishNumber++;
        finishNumberNodeMap.put(curFinishNumber, vertex.getVertexNumber());
    }

    private void dfs(Vertex vertex, List<Vertex> component){
        vertex.setIsExplored(true);
        component.add(vertex);

        for(Vertex v : vertex.getChildVertexes()){
            if(!v.isExplored())
                dfs(v, component);
        }
    }

}
