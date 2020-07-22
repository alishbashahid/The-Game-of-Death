package com.afiniti.the_game_of_death;

/**
 * Human class represents human entity. Stays static in a habitat
 */
public class Human extends Entity {

    /**
     * Immunity: probability the human can contract virus 0.0 <= i <= 1.0
     */
    private float i;

    /**
     * The location where human is currently at.
     */

    /**
     * Location where human is at currently.
     */
    private Coordinates currentLocation;

    /**
     * Whether human is infected or not .
     */
    private boolean isInfected;

    /**
     * Time until infection wears out.
     */
    private int currentInfectionTime;

    /**
     * Constructor
     * @param n Name of human
     * @param home Home location of human
     * @param i Immunity: probability the human can contract virus
     */
    protected Human(String n, Coordinates home, float i) {
        super(n, home);
        this.currentLocation = home;
        setImmunity(i);
        this.isInfected = false;
        this.currentInfectionTime = 0;
    }

    /**
     * Returns immunity
     * @return immunity
     */
    public float getImmunity(){
        return this.i;
    }

    /**
     * Setter method for immunity.
     * @param i Immunity: probability the human can contract virus 0.0 <= i <= 1.0
     */
    public void setImmunity(float i){
        if (i>=0 && i<=1){
            this.i = i;
        }
        else {
            throw new IllegalArgumentException("Immunity is probability of contracting a virus, it must be 0.0 <= i <= 1.0. But was given " + i);
        }
    }

    /**
     * Method to check whether human has infection or not
     * @return infected or not
     */
    public boolean hasInfection(){
        return isInfected;
    }

    /**
     * Updates the current location of human
     * @param coordinates new location
     */
    public void updateCurrentLocation(Coordinates coordinates){
        this.currentLocation = coordinates;
    }

    /**
     * Get's current location of human
     * @return current location of human
     */
    public Coordinates getCurrentLocation(){
        return this.currentLocation.getCoordinates();
    }

    /**
     * Sets infection on human for t time
     * @param t infection time
     */
    public void setInfection(int t){
        if (!isInfected) {
            this.currentInfectionTime = t;
            isInfected = true;
        }
    }

    /**
     * Updates infection time by -1 after a tick
     */
    public void updateInfectionTime(){
        if (isInfected) {
            this.currentInfectionTime--;
            if (currentInfectionTime==0){
                removeInfection();
                System.out.println("Human: " + n + " is now uninfected at x: " + currentLocation.getX() + " y: " + currentLocation.getY());
            }
        }
    }

    /**
     * Removes infection.
     */
    public void removeInfection(){
        if (isInfected) {
            isInfected = false;
            currentInfectionTime = 0;
        }
    }
}
