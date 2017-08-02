package net.graphical.model.causality.model;

/**
 * Created by sli on 2/2/16.
 */
public class Estimation<T> {
    private T value;
    private double probability;

    public Estimation(T value, double probability) {
        this.value = value;
        this.probability = probability;
    }

    public T getValue() {
        return value;
    }

    public double getProbability() {
        return probability;
    }
}
