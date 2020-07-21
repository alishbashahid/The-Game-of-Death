package com.afiniti.the_game_of_death;

import java.util.*;

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
        updateInfectionTimer();
        moveHumans();
        checkInfections();
        isRunning = shouldRun();
    }

    @Override
    public void initialize() {
        initializeOffices();
        initializeHumans();
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

        while (temp <= h) {

            int x = random.nextInt(n);
            int y = random.nextInt(n);

            if (humans.containsKey(new Coordinates(x,y))) continue;

            Human human = new Human(Integer.toString(temp), new Coordinates(x,y), generateRandomImmunity());
            habitatEntities.add(human);
            humans.put(human.getCurrentLocation(),human);

            temp++;
            System.out.println(x+" "+y);
        }

        Human[] temp_humans = humans.values().toArray(new Human[h]);


        temp_humans[0].setInfection(t);
        temp_humans[1].setInfection(t);



    }

    private void updateInfectionTimer(){
        for (Map.Entry<Coordinates,Human> humanEntry: humans.entrySet()) {
            Human human = humanEntry.getValue();
            human.updateInfectionTime();
        }
    }

    public void checkInfections(){
        for (Map.Entry<Coordinates,Human> humanEntry: humans.entrySet()){
            Human human = humanEntry.getValue();

            if (human.hasInfection()){
                continue;
            }

            if (hasInfectedNearby(human)){
                float probability = random.nextInt(1001)/1000f;
                if (human.getImmunity()<probability){
                    System.out.println("Human: " + human.n + " Got Infected at x: " + human.getCurrentLocation().getX() + " y: " + human.getCurrentLocation().getY() + " for "+ t + " ticks");
                    human.setInfection(this.t);
                }
            }
        }
    }

    private boolean hasInfectedNearby(Human human){
        int current_x = human.getCurrentLocation().getX();
        int current_y = human.getCurrentLocation().getY();


        for (int x = -1; x<=1 ; x++){
            for (int y = -1; y<=1; y++){
                int temp_x = current_x+x;
                int temp_y = current_y+y;

                if (temp_x > n || temp_x < 0 || temp_y>n || temp_y < 0) {
                    continue;
                } else {
                    if (humans.containsKey(new Coordinates(temp_x,temp_y)) && humans.get(new Coordinates(temp_x,temp_y)).hasInfection()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void moveHumans(){
        HashMap<Coordinates,Human> humans_temp = new HashMap<>();
        List<Coordinates> toBeRemoved = new ArrayList<>();

        for (Map.Entry<Coordinates, Human> humanEntry: humans.entrySet()){
            if (humanEntry.getValue() instanceof WorkingHuman){
                WorkingHuman workingHuman = (WorkingHuman) humanEntry.getValue();
                if (workingHuman.getCurrentLocation().equals(workingHuman.getOffice().getHomeLocation())) {
                    workingHuman.headToHome();
                    System.out.println("Human: " + workingHuman.n + " Reached Office at x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());
                    System.out.println("Human: " + workingHuman.n + " Will Now Head to Home");
                }
                else if (workingHuman.getCurrentLocation().equals(workingHuman.getHomeLocation())) {
                    System.out.println("Human: " + workingHuman.n + " Reached Home at x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());
                    System.out.println("Human: " + workingHuman.n + " Will Now Head to Office");
                    workingHuman.headToOffice();
                }

                int current_x = workingHuman.getCurrentLocation().getX();
                int current_y = workingHuman.getCurrentLocation().getY();

                int dest_x = workingHuman.getDestination().getX();
                int dest_y = workingHuman.getDestination().getY();

                int delta_x = dest_x-current_x;
                int delta_y = dest_y-current_y;

                int step_x = (delta_x>0 || delta_x<0)?delta_x/Math.abs(delta_x):0;
                int step_y = (delta_y>0 || delta_y<0)?delta_y/Math.abs(delta_y):0;

                int movement = random.nextInt(2);

                toBeRemoved.add(humanEntry.getKey());

                if (movement == 0 && step_x!=0 || step_y==0){
                    workingHuman.updateCurrentLocation(new Coordinates(current_x+step_x,current_y));
                }

                if (movement == 1 && step_y!=0 || step_x==0){
                    workingHuman.updateCurrentLocation(new Coordinates(current_x,current_y+step_y));
                }

                System.out.println("Human: " + workingHuman.n + " Moved To x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());

                humans_temp.put(workingHuman.getCurrentLocation(),workingHuman);

            }
        }

        for (Coordinates c: toBeRemoved){
            humans.remove(c);
        }

        humans.putAll(humans_temp);
    }

    private boolean shouldRun(){
        for (Human h: humans.values()){
            if (h.hasInfection()){
                return true;
            }
        }
        return false;
    }

    private float generateRandomImmunity(){
        return random.nextInt(1001)/1000f;
    }
}
