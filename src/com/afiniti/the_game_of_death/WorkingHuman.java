package com.afiniti.the_game_of_death;

/**
 * WorkingHuman class represents human entity that goes to office.
 */
public class WorkingHuman extends Human {

    /**
     * The office this human has to go to.
     */
    private Office office;

    /**
     * Destination to where this human is now headed
     */
    private Coordinates destination;

    /**
     * Constructor
     * @param n Name of human
     * @param home Home location of human
     * @param i Immunity: probability the human can contract virus
     * @param office Office the human has to go to
     */
    protected WorkingHuman(String n, Coordinates home, float i, Office office) {
        super(n, home, i);
        this.office = office;
        destination = new Coordinates(office.homeLocation.getX(),office.getHomeLocation().getY());
    }

    /**
     * Getter method to get office human works at.
     * @return office human works at
     */
    public Office getOffice(){
        return this.office;
    }

    /**
     * Getter method to get destination
     * @return Destination
     */
    public Coordinates getDestination(){
        return this.destination.getCoordinates();
    }

    /**
     * Switch destination to home
     */
    public void headToHome(){
        this.destination = this.homeLocation;
    }

    /**
     * Switch destination to office
     */
    public void headToOffice(){
        this.destination = this.office.getHomeLocation();
    }

}
