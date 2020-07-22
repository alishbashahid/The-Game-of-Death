package com.afiniti.the_game_of_death;

import java.util.*;

public class Covid19Simulation extends DiseaseSimulation{

    /**
     * Number of humans
     */
    private int h;

    /**
     * Number of working humans
     */
    private int w;

    /**
     * Number of offices
     */
    private int o;

    /**
     * Amount time of infection
     */
    private int t;

    /**
     * Percentage of initial infections
     */
    private float p;

    /**
     * Random object to generate random number
     */
    private Random random;

    /**
     * HashMap of offices <key: Coordinates,value: Office>
     */
    private HashMap<Coordinates,Office> offices;

    /**
     * HashMap of humans <key: Coordinates (Current Location), value: Office>
     */
    private HashMap<Coordinates,Human> humans;


    /**
     * Constructor
     * @param n Size of habitat
     * @param h Number of humans 0 <= h <= 1000000
     * @param w Number of Working Humans 0 <= w <= h
     * @param o Number of Offices 0 <= o <= 10000
     * @param t Amount time of infection
     */
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

        if (t < 1) {
            throw new IllegalArgumentException("Infection time must be greater than or equal to 1.");
        }

        this.h = h;
        this.w = w;
        this.o = o;
        this.t = t;
        random = new Random();
        offices = new HashMap<>();
        humans = new HashMap<>();
        this.p = 1.0f; // Default percentage of infection 1%
    }

    /**
     * Constructor
     * @param n Size of habitat
     * @param h Number of humans 0 <= h <= 1000000
     * @param w Number of Working Humans 0 <= w <= h
     * @param o Number of Offices 0 <= o <= 10000
     * @param t Amount time of infection
     * @param p Percentage of initial infections
     */
    public Covid19Simulation(long n, int h, int w, int o, int t, float p){
        this(n,h,w,o,t);

        if (p > 100f || p < 0f){
            throw new IllegalArgumentException("Infection percentage must be 0.0 <= p <=100.0. But was " + p);
        }

        this.p = p;
    }

    /**
     * Method to move simulation forward a step.
     */
    @Override
    public void tick() {
        updateInfectionTimer(); // Update infection time of every infected human
        moveHumans(); // Move working humans to destination either home or office
        checkInfections(); // After Movement check vicinity of each human and check for infected human
        isRunning = shouldRun(); // Update if everyone is uninfected or not to keep simulation running
    }

    /**
     * method to initialize parameters of simulation and populate habitat.
     */
    @Override
    public void initialize() {
        initializeOffices();
        initializeHumans();
    }

    /**
     * Method to initialize offices in the habitat.
     */
    public void initializeOffices(){
        int block_size = getBlockSize(n,o); // Get block size that would fit 'o' small blocks in habitat of size 'n'.

        //Initially start of block is origin x:0 y:0
        int start_x = 0;
        int start_y = 0;

        //End of the block x:block size y:block size
        int end_x = block_size;
        int end_y = block_size;

        int temp = 1;

        while (temp <= o) {
            //Get random x and y from the selected block not from entire habitat
            int x = start_x+random.nextInt(end_x-start_x);
            int y = start_y+random.nextInt(end_y-start_y);

            // Update start_x and end_x to move block horizontally in habitat
            start_x += block_size;
            end_x += block_size;

            // If reached end of habitat
            if(end_x>n){
                //Reset start_x and end_x to begining of the habitat
                start_x = 0;
                end_x = block_size;

                //Update start_y and end_y to move down the block vertically
                start_y += block_size;
                end_y += block_size;

                // If reached end of habitat
                if (end_y>n){
                    // Reset start_x, end_x, start_y and end_y so that block is again at start of the habitat.
                    start_x = 0;
                    end_x = block_size;
                    start_y = 0;
                    end_y = block_size;
                }
            }

            // if location is already taken by another office, continue!
            if (offices.containsKey(new Coordinates(x,y))){
                continue;
            }

            // Create office and put to HashMap
            Office office = new Office(Integer.toString(temp), new Coordinates(x,y));
            habitatEntities.add(office);
            offices.put(office.getHomeLocation(),office);

            temp++;

            //Log office initialized event
            System.out.println("Office initialized at x: " + x + " y: " +y);
        }
    }

    /**
     * Method to initialize humans in the habitat.
     */
    private void initializeHumans(){
        int block_size = getBlockSize(n,h); // Get block size that would fit 'h' small blocks in habitat of size 'n'.

        //Initially start of block is origin x:0 y:0
        int start_x = 0;
        int start_y = 0;

        //End of the block x:block size y:block size
        int end_x = block_size;
        int end_y = block_size;

        int temp = 1;

        Office[] temp_offices = offices.values().toArray(new Office[o]);

        while (temp <= w) {
            //Get random x and y from the selected block not from entire habitat
            int x = start_x+random.nextInt(end_x-start_x);
            int y = start_y+random.nextInt(end_y-start_y);

            // Update start_x and end_x to move block horizontally in habitat
            start_x += block_size;
            end_x += block_size;

            // If reached end of habitat
            if(end_x>n){
                //Reset start_x and end_x to begining of the habitat
                start_x = 0;
                end_x = block_size;

                //Update start_y and end_y to move down the block vertically
                start_y += block_size;
                end_y += block_size;

                // If reached end of habitat
                if (end_y>n){
                    // Reset start_x, end_x, start_y and end_y so that block is again at start of the habitat.
                    start_x = 0;
                    end_x = block_size;
                    start_y = 0;
                    end_y = block_size;
                }
            }

            // if location is already taken by another office or human, continue!
            if (humans.containsKey(new Coordinates(x,y)) || offices.containsKey(new Coordinates(x,y))) {
                continue;
            }

            // Randomly picking office from offices array
            Office assigned_office = temp_offices[random.nextInt(o)];

            // Creating workinghuman object and putting it to hashmap
            WorkingHuman human = new WorkingHuman(Integer.toString(temp), new Coordinates(x,y), generateRandomImmunity(), assigned_office);
            habitatEntities.add(human);
            humans.put(human.getCurrentLocation(),human);

            temp++;

            //Log working human initialized event
            System.out.println("Working human "+ human.getName() + " initialized at x: " + x + " y: " +y + " Immunity: " + human.getImmunity() + " Office x: " + assigned_office.homeLocation.getX() + " y: " + assigned_office.homeLocation.getY());
        }

        while (temp <= h) {
            //Get random x and y from the selected block not from entire habitat
            int x = start_x+random.nextInt(end_x-start_x);
            int y = start_y+random.nextInt(end_y-start_y);

            // Update start_x and end_x to move block horizontally in habitat
            start_x += block_size;
            end_x += block_size;

            // If reached end of habitat
            if(end_x>n){
                //Reset start_x and end_x to begining of the habitat
                start_x = 0;
                end_x = block_size;

                //Update start_y and end_y to move down the block vertically
                start_y += block_size;
                end_y += block_size;

                // If reached end of habitat
                if (end_y>n){
                    // Reset start_x, end_x, start_y and end_y so that block is again at start of the habitat.
                    start_x = 0;
                    end_x = block_size;
                    start_y = 0;
                    end_y = block_size;
                }
            }

            // if location is already taken by another office or human, continue!
            if (humans.containsKey(new Coordinates(x,y)) || offices.containsKey(new Coordinates(x,y))) {
                continue;
            }

            // Creating human object and putting it to hashmap
            Human human = new Human(Integer.toString(temp), new Coordinates(x,y), generateRandomImmunity());
            habitatEntities.add(human);
            humans.put(human.getCurrentLocation(),human);

            temp++;

            //Log human initialized event
            System.out.println("Human "+ human.getName() + " initialized at x: " + x + " y: " +y + " Immunity: " + human.getImmunity());
        }

        //Allocating working humans to offices
        Map<Coordinates, Human> temp_humans = new HashMap<>();

        //List of coordinates keys which will be removed from humans hashmap
        List<Coordinates> toBeRemoved = new ArrayList<>();

        // moving all working humans initially to offices
        for (Map.Entry<Coordinates, Human> humanEntry: humans.entrySet()){
            if (humanEntry.getValue() instanceof WorkingHuman){
                WorkingHuman workingHuman = (WorkingHuman)humanEntry.getValue();

                // Updating their current location to offices location
                workingHuman.updateCurrentLocation(workingHuman.getOffice().getHomeLocation());

                temp_humans.put(workingHuman.getCurrentLocation(),workingHuman);

                toBeRemoved.add(humanEntry.getKey());
            }
        }

        // Removing humans
        for (Coordinates c: toBeRemoved){
            humans.remove(c);
        }

        // Adding them again with updated coordinates
        humans.putAll(temp_humans);

        // Calculating initial number of infected humans
        int initial_infections = (int)Math.ceil(h*p/100f);

        // Infecting humans
        for (Human human: humans.values()){
            human.setInfection(t);
            System.out.println("Human "+ human.getName() + " initially Infected at x: " + human.getCurrentLocation().getX() + " y: " + human.getCurrentLocation().getY() + " Immunity: " + human.getImmunity());
            initial_infections--;
            if (initial_infections==0) break;
        }
    }

    /**
     * Method to update infection timer of infected humans
     */
    private void updateInfectionTimer(){
        for (Map.Entry<Coordinates,Human> humanEntry: humans.entrySet()) {
            Human human = humanEntry.getValue();
            human.updateInfectionTime();
        }
    }

    /**
     * Method to check infected humans in vicinity of all humans
     */
    public void checkInfections(){
        for (Map.Entry<Coordinates,Human> humanEntry: humans.entrySet()){
            Human human = humanEntry.getValue();

            // If already has infection, skip!
            if (human.hasInfection()){
                continue;
            }

            // if any human has infected human nearby
            if (hasInfectedNearby(human)){
                // Calculate chance of getting infected
                float probability = random.nextInt(1001)/1000f;

                // if less than immunity
                if (human.getImmunity()<probability){
                    // Infect human

                    // Log human infected
                    System.out.println("Human: " + human.getName() + " Got Infected at x: " + human.getCurrentLocation().getX() + " y: " + human.getCurrentLocation().getY() + " for "+ t + " ticks");

                    // Set timer and infection
                    human.setInfection(this.t);
                }
            }
        }
    }

    /**
     * Method to check whether a human has infected human in it's vicinity
     * @param human human to check infected humans around
     * @return has infected human in adjacent 8 cells or not
     */
    private boolean hasInfectedNearby(Human human){
        // Get current X and Y coordinates
        int current_x = human.getCurrentLocation().getX();
        int current_y = human.getCurrentLocation().getY();

        // Loop  through adjacent cells
        for (int x = -1; x<=1 ; x++){
            for (int y = -1; y<=1; y++){
                int temp_x = current_x+x;
                int temp_y = current_y+y;

                // Check edge cases off the habitat
                if (temp_x > n || temp_x < 0 || temp_y>n || temp_y < 0) {
                    continue;
                } else {
                    // If adjacent cell at x and y has a human and is infected
                    if (humans.containsKey(new Coordinates(temp_x,temp_y)) && humans.get(new Coordinates(temp_x,temp_y)).hasInfection()){
                        return true;
                    }
                }
            }
        }
        // if no infected human found
        return false;
    }

    /**
     * Method to move working humans towards their destination
     */
    public void moveHumans(){
        // Since we have to update the keys after movement we have to temporarily store them, delete old keys, and put humans with new keys of coordinates
        HashMap<Coordinates,Human> humans_temp = new HashMap<>();
        List<Coordinates> toBeRemoved = new ArrayList<>();

        // Looping through all humans
        for (Map.Entry<Coordinates, Human> humanEntry: humans.entrySet()){
            // If human is working human
            if (humanEntry.getValue() instanceof WorkingHuman){
                WorkingHuman workingHuman = (WorkingHuman) humanEntry.getValue();

                // If human has reached office
                if (workingHuman.getCurrentLocation().equals(workingHuman.getOffice().getHomeLocation())) {
                    // Switch destination to home
                    workingHuman.headToHome();

                    // Log reached office event
                    System.out.println("Human: " + workingHuman.getName() + " Reached Office at x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());
                    System.out.println("Human: " + workingHuman.getName() + " Will Now Head to Home");
                }
                // If human has reached home
                else if (workingHuman.getCurrentLocation().equals(workingHuman.getHomeLocation())) {
                    // Switch destination to office
                    workingHuman.headToOffice();

                    // Log reached home event
                    System.out.println("Human: " + workingHuman.getName() + " Reached Home at x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());
                    System.out.println("Human: " + workingHuman.getName() + " Will Now Head to Office");
                }

                // Current X and Y
                int current_x = workingHuman.getCurrentLocation().getX();
                int current_y = workingHuman.getCurrentLocation().getY();

                // Destination X and Y
                int dest_x = workingHuman.getDestination().getX();
                int dest_y = workingHuman.getDestination().getY();

                // Difference between current X and Y and destination X and Y
                int delta_x = dest_x-current_x;
                int delta_y = dest_y-current_y;

                // Since we can only take one step, calculate one step in X and Y towards destination
                int step_x = (delta_x>0 || delta_x<0)?delta_x/Math.abs(delta_x):0;
                int step_y = (delta_y>0 || delta_y<0)?delta_y/Math.abs(delta_y):0;

                // Randomly decide whether move in X direction or Y direction. 0 for X, 1 for Y
                int movement = random.nextInt(2);

                // Should Go in direction of X
                boolean go_x = true;
                // Should Go in direction of y
                boolean go_y = true;

                // Calculate new coordinates based on step taken in x and y direction
                Coordinates new_coordinates_x = new Coordinates(current_x+step_x,current_y);
                Coordinates new_coordinates_y = new Coordinates(current_x,current_y+step_y);

                // If there is no office in X direction
                if (!offices.containsKey(new_coordinates_x)) {
                    // If there is human in that direction
                    if (humans.containsKey(new_coordinates_x)) {
                        // Switch movement to go in Y direction
                        go_x = false;
                        movement = 1;
                    }
                }

                // If there is no office in Y direction
                if (!offices.containsKey(new_coordinates_y)) {
                    // If there is human in that direction
                    if (humans.containsKey(new_coordinates_y)) {
                        // Switch movement to go in X direction
                        go_y = false;
                        movement = 0;
                    }
                }

                // Move in direction of X if there is office in that direction, no human, human has decided to move in X or human doesn't have to move in Y anymore
                if (movement == 0 && step_x!=0 || step_y==0 && go_x){
                    // Update coordinates
                    workingHuman.updateCurrentLocation(new_coordinates_x);

                    // Log human movement
                    System.out.println("Human: " + workingHuman.getName() + " Moved To x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());
                }

                // Move in direction of Y if there is office in that direction, no human, human has decided to move in Y or human doesn't have to move in X anymore
                if (movement == 1 && step_y!=0 || step_x==0 && go_y){
                    // Update coordinates
                    workingHuman.updateCurrentLocation(new_coordinates_y);

                    // Log human movement
                    System.out.println("Human: " + workingHuman.getName() + " Moved To x: " + workingHuman.getCurrentLocation().getX() + " y: " + workingHuman.getCurrentLocation().getY());
                }

                toBeRemoved.add(humanEntry.getKey());
                humans_temp.put(workingHuman.getCurrentLocation(),workingHuman);

            }
        }

        // Update humans hashmap with new coordinates
        for (Coordinates c: toBeRemoved){
            humans.remove(c);
        }

        humans.putAll(humans_temp);
    }

    /**
     * Method to Check if all humans have become uninfected or not.
     * @return all humans uninfected or not to keep simulation running
     */
    private boolean shouldRun(){
        for (Human h: humans.values()){
            if (h.hasInfection()){
                return true;
            }
        }
        return false;
    }

    /**
     * Method to get block size that would fit a number of small blocks of that size in habitat of size 'n'.
     *
     * Code taken from 'kenneth' at: https://stackoverflow.com/questions/868997/max-square-size-for-unknown-number-inside-rectangle/1656634#1656634
     *
     * @param n Habitat Size
     * @param block_count Number of smaller blocks to fit
     * @return Size of small block that can fit in habitat.
     */
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

    /**
     * Method to generate random immunity from 0.0 to 1.0.
     * @return random value from 0.0 to 1.0
     */
    private float generateRandomImmunity(){
        return (float)(random.nextInt(1001)/1000.0);
    }
}
