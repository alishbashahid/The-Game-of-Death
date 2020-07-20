package com.afiniti.the_game_of_death;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Coordinates getCoordinates(){
        return new Coordinates(x, y);
    }
}
