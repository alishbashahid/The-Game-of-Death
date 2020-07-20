package com.afiniti.the_game_of_death;

public abstract class Entity {
    protected String n;
    protected Coordinates homeLocation;

    protected Entity(String n, Coordinates home){
        this.n = n;
        this.homeLocation = home;
    }

    public String getName(){
        return this.n;
    }

    public Coordinates getHomeLocation(){
        return this.homeLocation.getCoordinates();
    }
}
