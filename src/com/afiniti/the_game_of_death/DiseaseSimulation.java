package com.afiniti.the_game_of_death;

import java.util.ArrayList;
import java.util.List;

/**
 * DiseaseSimulation abstract class can be inherited to create a simulation of choice with different parameters.
 */
public abstract class DiseaseSimulation {
    /**
     * List of all the entities in a habitat.
     */
    protected List<Entity> habitatEntities;

    /**
     * Whether simulation is running or not.
     */
    public boolean isRunning;

    /**
     * Habitat size in 2D n^2
     */
    protected long n;

    /**
     * Constructor
     * @param n size of habitat
     */
    protected DiseaseSimulation(long n){
        if (n<1 && n>1000000){
            throw new IllegalArgumentException("Habitat size n must be 0 < n <=1000000");
        }

        this.n = n;
        habitatEntities = new ArrayList<>();
        isRunning = true;
    }

    /**
     * method to move simulation forward a step.
     */
    public abstract void tick();

    /**
     * method to initialize parameters of simulation and populate habitat.
     */
    public abstract void initialize();
}
