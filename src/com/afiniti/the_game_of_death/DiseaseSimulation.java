package com.afiniti.the_game_of_death;

import java.util.ArrayList;
import java.util.List;

public abstract class DiseaseSimulation {
    protected List<Entity> habitatEntities;
    public boolean isRunning;
    protected int n;

    protected DiseaseSimulation(int n){
        this.n = n;
        habitatEntities = new ArrayList<>();
        isRunning = true;
    }

    public abstract void tick();

    public abstract void initialize();
}
