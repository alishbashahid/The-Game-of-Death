package com.afiniti.the_game_of_death;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Covid19Simulation extends DiseaseSimulation{

    private int h;
    private int w;
    private int o;
    private int t;
    private Random random;
    private HashMap<Coordinates,Office> offices;
    private HashMap<Coordinates,Human> humans;


    public Covid19Simulation(int n, int h, int w, int o, int t){
        super(n);
        this.h = h;
        this.w = w;
        this.o = o;
        this.t = t;
        random = new Random();
        offices = new HashMap<>();
        humans = new HashMap<>();
    }

    @Override
    public void tick() {

    }

    @Override
    public void initialize() {

    }

    public void initializeOffices(){
        int temp = 1;

        while (temp <= o) {

            int x = random.nextInt(n);
            int y = random.nextInt(n);

            if (offices.containsKey(new Coordinates(x,y))) continue;

            Office office = new Office(Integer.toString(temp), new Coordinates(x,y));
            habitatEntities.add(office);
            offices.put(office.getHomeLocation(),office);

            temp++;
            System.out.println(x+" "+y);
        }
        System.out.println(offices.size());
    }

    private void initializeHumans(){
        int temp = 1;
        Office[] temp_offices = offices.values().toArray(new Office[o]);

        while (temp <= w) {

            int x = random.nextInt(n);
            int y = random.nextInt(n);

            if (humans.containsKey(new Coordinates(x,y))) continue;

            //TODO: make offices initially occupied
            WorkingHuman human = new WorkingHuman(Integer.toString(temp), new Coordinates(x,y), generateRandomImmunity(), temp_offices[random.nextInt(o)]);
            habitatEntities.add(human);
            humans.put(human.getHomeLocation(),human);

            temp++;
            System.out.println(x+" "+y);
        }

        temp = 1;
        int remaining_humans = h-w;

        while (temp <= remaining_humans) {

            int x = random.nextInt(n);
            int y = random.nextInt(n);

            if (humans.containsKey(new Coordinates(x,y))) continue;

            Human human = new Human(Integer.toString(temp), new Coordinates(x,y), generateRandomImmunity());
            habitatEntities.add(human);
            humans.put(human.getHomeLocation(),human);

            temp++;
            System.out.println(x+" "+y);
        }
    }

    private float generateRandomImmunity(){
        return random.nextInt(1001)/1000f;
    }
}
