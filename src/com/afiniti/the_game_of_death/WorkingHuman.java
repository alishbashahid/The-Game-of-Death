package com.afiniti.the_game_of_death;

public class WorkingHuman extends Human {

    private Office o;

    protected WorkingHuman(String n, Coordinates home, int i, Office o) {
        super(n, home, i);
        this.o = o;
    }

    public Office getOffice(){
        return this.o;
    }
}
