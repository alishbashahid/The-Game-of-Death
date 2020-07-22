package com.afiniti.the_game_of_death;

public class Driver {

    public static void main(String[] args) throws InterruptedException {
        int n = 0;
        int h = 0;
        int w = 0;
        int o = 0;
        int t = 0;
        float p = 1.0f;
        DiseaseSimulation ds = null;

        try{
            if (args.length == 6) {
                n = Integer.parseInt(args[0]);
                h = Integer.parseInt(args[1]);
                w = Integer.parseInt(args[2]);
                o = Integer.parseInt(args[3]);
                t = Integer.parseInt(args[4]);
                p = Float.parseFloat(args[5]);
                ds = new Covid19Simulation(n, h, w,o,t, p);
            } else if (args.length == 5) {
                n = Integer.parseInt(args[0]);
                h = Integer.parseInt(args[1]);
                w = Integer.parseInt(args[2]);
                o = Integer.parseInt(args[3]);
                t = Integer.parseInt(args[4]);
                ds = new Covid19Simulation(n, h, w,o,t);
            } else {
                System.out.println("Usage: <Integer:n> <Integer:h> <Integer:w> <Integer:o> <Integer:t> || [Float:p]");
                return;
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Usage: <Integer:n> <Integer:h> <Integer:w> <Integer:o> <Integer:t> || [Float:p]");
            return;
        } catch (IllegalArgumentException iae){
            iae.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ds.initialize();

        while (ds.isRunning){
            Thread.sleep(100);
            ds.tick();
        }
    }
}
