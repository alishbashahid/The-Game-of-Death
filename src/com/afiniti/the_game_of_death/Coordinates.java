package com.afiniti.the_game_of_death;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y){
        if (x<0 || y<0){
            throw new IllegalArgumentException("X and Y cannot have negative coordinates.");
        }

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
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Coordinates){
            Coordinates temp = (Coordinates)obj;
            if (temp.getX()==getX() && temp.getY()==getY()) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int temp = (y+((x+1)/2));
        return x + (temp*temp);
    }
}
