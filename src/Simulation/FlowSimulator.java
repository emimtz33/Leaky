package Simulation;

import java.util.Random;

import Model.FlowReading;

public class FlowSimulator implements FlowSource{

    private int currentMinute;
    private double currentFlow;
    private final Random random;

    private boolean active;
    private int remainingMinutes;

    private WaterActivity currentActivity;

    public FlowSimulator(){
        this.currentMinute = 0;
        this.random = new Random();
        restart();
    }

    @Override 
    public FlowReading getReading(){
        currentMinute++;
        double probability = 0;
        //First decide if we start an activity
        if (!active){
            probability = random.nextDouble();

            if (probability < 0.25){startActivity(WaterActivity.SHOWER);}
            else if (probability < 0.3){startActivity(WaterActivity.WASHING_MACHINE);} 
            else if (probability < 0.4) {startActivity(WaterActivity.DISHWASHING);}

        }
        //Create normal flow
        double flow = generateNormalFlow(); //We are always looking for normal flow
        //If we started an activity then add its to the flow
        if (active){
            flow += currentFlow; //Activity flow + normal flow
            remainingMinutes--;
            if (remainingMinutes == 0){stopActivity();}
        }
        
        return new FlowReading(currentMinute, flow, probability); 
    }



    private double generateNormalFlow(){
        double probability = random.nextDouble();

        //65% chance of no water flow
        if (probability < 0.65){
            return 0.0;
        }

        //20% chance of low flow
        if (probability < 0.85){
            return 2 + random.nextDouble() * 4;
        }
        
        return 8 + random.nextDouble() * 7;
    }




     public void startActivity(WaterActivity activity) {
        if (active) {
            throw new IllegalStateException("An activity is already running");
        }

        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }
        currentActivity = activity;

        switch (activity) {

            case SHOWER:
                remainingMinutes = 12;
                currentFlow = 10 + random.nextDouble() * 5;
                currentActivity = WaterActivity.SHOWER;
                break;

            case WASHING_MACHINE:
                remainingMinutes = 45;
                currentFlow = 3 + random.nextDouble() * 5;
                currentActivity = WaterActivity.WASHING_MACHINE;
                break;

            case DISHWASHING:
                remainingMinutes = 8;
                currentFlow = 2 + random.nextDouble() * 4;
                currentActivity = WaterActivity.DISHWASHING;
                break;
        }

        active = true;
    }



    private void stopActivity() {
        active = false;
        remainingMinutes = 0;
        currentFlow = 0;
        currentActivity = null;
    }


    public boolean isActive() {
        return active;
    }

    public WaterActivity getCurrentActivity() {
        return currentActivity;
    }

    public int getRemainingMinutes() {
        return remainingMinutes;
    }

    @Override
    public void restart(){
        currentMinute = 0;
    }
}