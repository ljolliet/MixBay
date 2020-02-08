package fr.pdp.mixbay.models;

public class RandomAlgo implements AlgoI {

    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public Playlist compute() {
        return null;
    }

}