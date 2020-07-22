package com.afiniti.the_game_of_death;

/**
 * Entity Class represents an entity on a Habitat
 */
public abstract class Entity {
    protected String n;
    protected Coordinates homeLocation;

    /**
     *
     * @param n Name of Entity
     * @param home Location of Entity in a Habitat
     */
    protected Entity(String n, Coordinates home){
        this.n = n;
        this.homeLocation = home;
    }

    /**
     * Getter method to get name 'n'
     * @return String Name 'n'
     */
    public String getName(){
        return this.n;
    }

    /**
     * Getter method to get location 'home'
     * @return Coordinates 'home'
     */
    public Coordinates getHomeLocation(){
        return this.homeLocation.getCoordinates();
    }
}
