package com.afiniti.the_game_of_death;

import java.util.*;

/**
 * Office class represents Office in a habitat
 */
public class Office extends Entity {

    /**
     * List of humans currently at office
     */
    private List<Human> humansInOffice;

    /**
     * Constructor
     * @param n Name of office
     * @param home Location of office
     */
    protected Office(String n, Coordinates home) {
        super(n, home);
        humansInOffice = new ArrayList<>();
    }

    /**
     * Add human to office
     * @param human Human to add
     */
    public void addHuman(Human human){
        humansInOffice.add(human);
    }

    /**
     * Remove human from office
     * @param human
     */
    public void removeHuman(Human human){
        humansInOffice.remove(human);
    }

    /**
     * Get list of humans in office
     * @return ArrayList of humans
     */
    public List<Human> getHumansInOffice(){
        return humansInOffice;
    }


}
