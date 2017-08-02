package net.ipsoft.ipcenter.iprca.causality.graph.model;

/**
 * Created by sli on 10/27/15.
 */
public enum EdgeType {
    UNDIRECTED {
        public EdgeType opposite(){return UNDIRECTED;}
        public boolean isDirected() {return false;}
        public boolean isDirectedMinus() { return false;}
        public boolean isDirectedPlus() { return false;}
    },
    DIRECTED_PLUS{
        public EdgeType opposite(){return DIRECTED_MINUS;}
        public boolean isDirected() {return true;}
        public boolean isDirectedMinus() { return false;}
        public boolean isDirectedPlus() { return true;}
    },
    DIRECTED_MINUS{
        public EdgeType opposite(){return DIRECTED_PLUS;}
        public boolean isDirected() {return true;}
        public boolean isDirectedMinus() { return true;}
        public boolean isDirectedPlus() { return false;}
    };

    public abstract EdgeType opposite();
    public abstract boolean isDirected();
    public abstract boolean isDirectedMinus();
    public abstract boolean isDirectedPlus();

}
