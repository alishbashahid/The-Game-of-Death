package com.afiniti.the_game_of_death;

import java.util.*;

/**
 * Office class represents Office in a habitat
 */
public class Office extends Entity {

    /**
     * Constructor
     * @param n Name of office
     * @param home Location of office
     */
    protected Office(String n, Coordinates home) {
        super(n, home);
    }
}
