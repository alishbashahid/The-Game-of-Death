package com.afiniti.the_game_of_death;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            humans.put(human.getCurrentLocation(),human);

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
            humans.put(human.getCurrentLocation(),human);

            temp++;
            System.out.println(x+" "+y);
        }
    }

    public void moveHumans(){
        HashMap<Coordinates,Human> humans_temp = new HashMap<>();

        for (Map.Entry<Coordinates, Human> humanEntry: humans.entrySet()){
            if (humanEntry.getValue() instanceof WorkingHuman){
                WorkingHuman workingHuman = (WorkingHuman) humanEntry.getValue();
                if (workingHuman.getCurrentLocation().equals(workingHuman.getOffice().getHomeLocation())) {
                    workingHuman.headToHome();
                }
                else if (workingHuman.getCurrentLocation().equals(workingHuman.getHomeLocation())) {
                    workingHuman.headToOffice();
                }

                int current_x = workingHuman.getCurrentLocation().getX();
                int current_y = workingHuman.getCurrentLocation().getX();

                int dest_x = workingHuman.getDestination().getX();
                int dest_y = workingHuman.getDestination().getY();

                int delta_x = dest_x-current_x;
                int delta_y = dest_y-current_y;

                int step_x = delta_x/Math.abs(delta_x);
                int step_y = delta_y/Math.abs(delta_y);

                int movement = random.nextInt(2);

                if (movement == 0 && step_x!=0 || step_y==0){
                    workingHuman.updateCurrentLocation(new Coordinates(current_x+step_x,current_y));
                }

                if (movement == 1 && step_y!=0 || step_x==0){
                    workingHuman.updateCurrentLocation(new Coordinates(current_x,current_y+step_y));
                }

                humans.remove(humanEntry.getKey());
                humans_temp.put(workingHuman.getCurrentLocation(),workingHuman);

            }
        }
        humans.putAll(humans_temp);
    }

    private float generateRandomImmunity(){
        return random.nextInt(1001)/1000f;
    }
}
