package com.afiniti.the_game_of_death;

import java.util.*;

public class Office extends Entity {

    private List<WorkingHuman> employees;

    protected Office(String n, Coordinates home) {
        super(n, home);
        this.employees = new ArrayList();
    }

    public void addEmployee(WorkingHuman workingHuman){
        this.employees.add(workingHuman);
    }

    public void removeEmployee(WorkingHuman workingHuman){
        this.employees.remove(workingHuman);
    }
}
