package com.afiniti.the_game_of_death;

public class WorkingHuman extends Human {

    private Office office;
    private Coordinates destination;

    protected WorkingHuman(String n, Coordinates home, float i, Office office) {
        super(n, home, i);
        this.office = office;
        destination = new Coordinates(office.homeLocation.getX(),office.getHomeLocation().getY());
    }

    public Office getOffice(){
        return this.office;
    }

    public Coordinates getDestination(){
        return this.destination.getCoordinates();
    }

    public void headToHome(){
        this.destination = this.homeLocation;
    }

    public void headToOffice(){
        this.destination = this.office.getHomeLocation();
    }

}
