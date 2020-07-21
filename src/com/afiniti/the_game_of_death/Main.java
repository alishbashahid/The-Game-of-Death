package com.afiniti.the_game_of_death;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        DiseaseSimulation ds = new Covid19Simulation(5,8,4,2,30);

        ds.initialize();

        while (ds.isRunning) {
            ds.tick();
        }
    }
}
