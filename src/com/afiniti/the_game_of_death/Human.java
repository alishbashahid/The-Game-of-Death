package com.afiniti.the_game_of_death;

public class Human extends Entity {

    private float i;
    private Coordinates currentLocation;

    protected Human(String n, Coordinates home, int i) {
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
        else{
            throw new IllegalArgumentException("Immunity is probability of contracting a virus, its value lies between 0 and 1 inclusive.");
        }
    }

    public Coordinates getCurrentLocation(){
        return this.currentLocation.getCoordinates();
    }
}
