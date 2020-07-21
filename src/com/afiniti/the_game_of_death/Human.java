package com.afiniti.the_game_of_death;

public class Human extends Entity {

    private float i;
    private Coordinates currentLocation;
    private boolean isInfected;
    private int currentInfectionTime;

    protected Human(String n, Coordinates home, float i) {
        super(n, home);
        this.currentLocation = home;
    }

    public float getImmunity(){
        return this.i;
    }

    public void setImmunity(int i){
        if (i>=0 && i<=1){
            this.i = i;
        }
        else {
            throw new IllegalArgumentException("Immunity is probability of contracting a virus, its value lies between 0 and 1 inclusive.");
        }
    }

    public boolean hasInfection(){
        return isInfected;
    }

    public void updateCurrentLocation(Coordinates coordinates){
        this.currentLocation = coordinates;
    }

    public Coordinates getCurrentLocation(){
        return this.currentLocation.getCoordinates();
    }

    public void setInfection(int t){
        if (!isInfected) {
            this.currentInfectionTime = t;
            isInfected = true;
        }
    }

    public void updateInfectionTime(){
        if (isInfected) {
            this.currentInfectionTime--;
            if (currentInfectionTime==0){
                removeInfection();
            }
        }
    }

    public void removeInfection(){
        if (isInfected) {
            isInfected = false;
        }
    }
}
