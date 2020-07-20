package com.afiniti.the_game_of_death;

import java.util.ArrayList;
import java.util.List;

public abstract class DiseaseSimulation {
    protected List<Entity> habitatEntities;
    public boolean isRunning;
    protected int habitatSize;

    protected DiseaseSimulation(int n){
        this.habitatSize = n;
        habitatEntities = new ArrayList<>();
    }

    public abstract void tick();

    public abstract void initialize();
}
