package com.afiniti.the_game_of_death;

/**
 * Coordinates class to represent a position
 */
public class Coordinates {
    /**
     * X coordinate
     */
    private int x;

    /**
     * Y coordinate
     */
    private int y;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Coordinates(int x, int y){
        if (x<0 || y<0){
            throw new IllegalArgumentException("X and Y cannot have negative coordinates.");
        }

        this.x = x;
        this.y = y;
    }

    /**
     * Returns x coordinate
     * @return x
     */
    public int getX(){
        return this.x;
    }

    /**
     * Returns y coordinate
     * @return y
     */
    public int getY(){
        return this.y;
    }

    /**
     * returns a reference to this object
     * @return this
     */
    public Coordinates getCoordinates(){
        return this;
    }

    /**
     * Overriding equals method for comparison
     * @param obj
     * @return equal or not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Coordinates){
            Coordinates temp = (Coordinates)obj;
            if (temp.getX()==getX() && temp.getY()==getY()) return true;
        }
        return false;
    }

    /**
     * Overriding hashCode method for storing this object as key in HashMap without collision
     * @return
     */
    @Override
    public int hashCode() {
        int temp = (y+((x+1)/2));
        return x + (temp*temp);
    }
}
