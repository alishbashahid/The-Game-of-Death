package com.afiniti.the_game_of_death;

import java.util.ArrayList;
import java.util.List;

public abstract class DiseaseSimulation {
    protected List<Entity> habitatEntities;
    public boolean isRunning;
    protected long n;

    protected DiseaseSimulation(long n){
        if (n<1 && n>1000000){
            throw new IllegalArgumentException("Habitat size n must be 0 < n <=1000000");
        }

        this.n = n;
        habitatEntities = new ArrayList<>();
        isRunning = true;
    }

    public abstract void tick();

    public abstract void initialize();
}
