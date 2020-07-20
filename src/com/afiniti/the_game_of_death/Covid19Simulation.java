package com.afiniti.the_game_of_death;

import java.util.HashMap;
import java.util.List;

public class Covid19Simulation extends DiseaseSimulation{

    private int h;
    private int w;
    private int o;
    private HashMap<Coordinates,Office> offices;
    private HashMap<Coordinates,Human> humans;


    public Covid19Simulation(int n, int h, int w, int o){
        super(n);
        this.h = h;
        this.w = w;
        this.o = o;
    }

    @Override
    public void tick() {

    }

    @Override
    public void initialize() {

    }

    private void initializeOffices(){

    }

    private void initializeHumans(){

    }


}
