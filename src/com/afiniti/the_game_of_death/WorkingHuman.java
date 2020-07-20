package com.afiniti.the_game_of_death;

public class WorkingHuman extends Human {

    private Office office;

    protected WorkingHuman(String n, Coordinates home, float i, Office office) {
        super(n, home, i);
        this.office = office;
    }

    public Office getOffice(){
        return this.office;
    }

    public void moveTowardsOffice(){

    }

    public void moveTowardsHome(){

    }
}
