package net.graphical.model.causality.model;

/**
 * Created by sli on 1/29/16.
 */
public class Pair<T1, T2>  implements Comparable{
    private T1 first;
    private T2 second;


    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public void setFirst(T1 first) {
        this.first = first;
    }

    public T2 getSecond() {
        return second;
    }

    public void setSecond(T2 second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {

        return toString().equals(o.toString());

    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return first.toString() + "|" + second.toString();
    }

    @Override
    public int compareTo(Object o) {
        String me = getSecond().toString() + "|" + getFirst().toString();
        String other = ((Pair) o).getSecond().toString() + "|" + ((Pair) o).getFirst().toString();
        return me.compareTo(other);
    }
}
