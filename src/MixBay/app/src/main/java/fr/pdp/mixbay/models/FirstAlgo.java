package fr.pdp.mixbay.models;

public class FirstAlgo implements AlgoI {


    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public Playlist compute() {
        return null;
    }
}
