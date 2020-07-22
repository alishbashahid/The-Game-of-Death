package com.afiniti.the_game_of_death;

import java.math.BigInteger;
import java.util.*;

public class Covid19Simulation extends DiseaseSimulation{

    private int h;
    private int w;
    private int o;
    private int t;
    private float p;
    private Random random;
    private HashMap<Coordinates,Office> offices;
    private HashMap<Coordinates,Human> humans;


    public Covid19Simulation(long n, int h, int w, int o, int t){
        super(n);

        if (h<0 && h>1000000){
            throw new IllegalArgumentException("Number of humans h must be 0 <= h <= 1000000. But was " + h);
        }

        if (w<0 && w>h){
            throw new IllegalArgumentException("Number of working humans h must be 0 <= w <= h. But was " + w);
        }

        if (o<0 && h>10000){
            throw new IllegalArgumentException("Number of offices o must be 0 <= o <= 10000. But was " + o);
        }

        if (h+o > n*n){
            throw new IllegalArgumentException("Number of entities (h+o) are greater than number of cells in a habitat n^2. Total entities: " + h+o);
        }

        this.h = h;
        this.w = w;
        this.o = o;
        this.t = t;
        random = new Random();
        offices = new HashMap<>();
        humans = new HashMap<>();
        this.p = 1.0f;
    }

    public Covid19Simulation(long n, int h, int w, int o, int t, float p){
        this(n,h,w,o,t);

        if (p > 100f || p<0f){
            throw new IllegalArgumentException("Infection percentage must be 0.0 <= p <=100.0. But was " + p);
        }

        this.p = p;
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
        int block_size = getBlockSize(n,o);

        int start_x = 0;
        int start_y = 0;

        int end_x = block_size;
        int end_y = block_size;

        int temp = 1;

        while (temp <= o) {

            int x = start_x+random.nextInt(end_x-start_x);
            int y = start_y+random.nextInt(end_y-start_y);

            start_x += block_size;
            end_x += block_size;

            if(end_x>n){
                start_x = 0;
                end_x = block_size;
                start_y += block_size;
                end_y += block_size;
                if (end_y>n){
                    start_x = 0;
                    end_x = block_size;
                    start_y = 0;
                    end_y = block_size;
                }
            }

            if (offices.containsKey(new Coordinates(x,y))){
                continue;
            }

            Office office = new Office(Integer.toString(temp), new Coordinates(x,y));
            habitatEntities.add(office);
            offices.put(office.getHomeLocation(),office);

            temp++;
            System.out.println(x+" "+y);
        }

    }

    private void initializeHumans(){
        int block_size = getBlockSize(n,h);

        int start_x = 0;
        int start_y = 0;

        int end_x = block_size;
        int end_y = block_size;

        int temp = 1;

        Office[] temp_offices = offices.values().toArray(new Office[o]);

        while (temp <= w) {

            int x = start_x+random.nextInt(end_x-start_x);
            int y = start_y+random.nextInt(end_y-start_y);

            start_x += block_size;
            end_x += block_size;

            if(end_x>n){
                start_x = 0;
                end_x = block_size;
                start_y += block_size;
                end_y += block_size;
                if (end_y>n){
                    start_x = 0;
                    end_x = block_size;
                    start_y = 0;
                    end_y = block_size;
                }
            }

            if (humans.containsKey(new Coordinates(x,y)) || offices.containsKey(new Coordinates(x,y))) {
                continue;
            }


            Office assigned_office = temp_offices[random.nextInt(o)];
            WorkingHuman human = new WorkingHuman(Integer.toString(temp), new Coordinates(x,y), generateRandomImmunity(), assigned_office);
            habitatEntities.add(human);
            humans.put(human.getCurrentLocation(),human);

            temp++;

            System.out.println(x+" "+y);
        }

        while (temp <= h) {

            int x = start_x+random.nextInt(end_x-start_x);
            int y = start_y+random.nextInt(end_y-start_y);

            start_x += block_size;
            end_x += block_size;

            if(end_x>n){
                start_x = 0;
                end_x = block_size;
                start_y += block_size;
                end_y += block_size;
                if (end_y>n){
                    start_x = 0;
                    end_x = block_size;
                    start_y = 0;
                    end_y = block_size;
                }
            }

            if (humans.containsKey(new Coordinates(x,y)) || offices.containsKey(new Coordinates(x,y))){
                continue;
            }

            Human human = new Human(Integer.toString(temp), new Coordinates(x,y), generateRandomImmunity());
            habitatEntities.add(human);
            humans.put(human.getCurrentLocation(),human);

            temp++;

            System.out.println(x+" "+y);
        }

        Map<Coordinates, Human> temp_humans = new HashMap<>();
        List<Coordinates> toBeRemoved = new ArrayList<>();

        for (Map.Entry<Coordinates, Human> humanEntry: humans.entrySet()){
            if (humanEntry.getValue() instanceof WorkingHuman){
                WorkingHuman workingHuman = (WorkingHuman)humanEntry.getValue();

                workingHuman.updateCurrentLocation(workingHuman.getOffice().getHomeLocation());
                temp_humans.put(workingHuman.getCurrentLocation(),workingHuman);

                toBeRemoved.add(humanEntry.getKey());
            }
        }

        for (Coordinates c: toBeRemoved){
            humans.remove(c);
        }

        humans.putAll(temp_humans);

        int initial_infections = (int)Math.ceil(h*p);

        for (Human human: humans.values()){
            human.setInfection(t);
            initial_infections--;
            if (initial_infections==0) break;
        }
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

                boolean go_x = true;
                boolean go_y = true;

                Coordinates new_coordinates_x = new Coordinates(current_x+step_x,current_y);
                Coordinates new_coordinates_y = new Coordinates(current_x,current_y+step_y);

                if (humans.containsKey(new_coordinates_x)){
                    go_x = false;
                    movement = 1;
                }

                if (humans.containsKey(new_coordinates_y)) {
                    go_y = false;
                    movement = 0;
                }

                if (movement == 0 && step_x!=0 || step_y==0 && go_x){
                    workingHuman.updateCurrentLocation(new_coordinates_x);
                    System.out.println("Human: " + workingHuman.n + " Moved To x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());
                }

                if (movement == 1 && step_y!=0 || step_x==0 && go_y){
                    workingHuman.updateCurrentLocation(new_coordinates_y);
                    System.out.println("Human: " + workingHuman.n + " Moved To x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());
                }

                toBeRemoved.add(humanEntry.getKey());
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

    private int getBlockSize(long n, int block_count)
    {
        //divide the area but the number of blocks to get the max area a block could cover
        //this optimal size for a block will more often than not make the blocks overlap, but
        //a block can never be bigger than this size
        long l_n = n;

        double maxSize = Math.sqrt(l_n*l_n/block_count);
        //find the number of whole blocks that can fit into the height
        double possibleBlocksinN = Math.floor(n / maxSize);
        //works out how many whole blocks this configuration can hold
        double total = possibleBlocksinN * possibleBlocksinN;

        //if the number of number of whole blocks that the max size block ends up with is less than the require number of
        //blocks, make the maxSize smaller and recaluate
        while(total < block_count){
            maxSize--;
            possibleBlocksinN = Math.floor(n / maxSize);
            total = possibleBlocksinN * possibleBlocksinN;
        }
        return (int)Math.ceil(maxSize);
    }

    private float generateRandomImmunity(){
        return random.nextInt(1001)/1000f;
    }
}
