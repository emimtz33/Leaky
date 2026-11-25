package Model;

public class FlowReading{
    //Final since we don't want previous readings to be modified
    private final int minute;
    private final double litersPerMintute;
    private final double activityProbability;

    public FlowReading(int minute, double litersPerMintute, double activityProbability){
        if (minute < 1){
            throw new IllegalArgumentException ("The minute must be over zero");
        }

        if (!Double.isFinite(litersPerMintute) || litersPerMintute < 0){
            throw new IllegalArgumentException ("The caudal must be a valid and positive number");
        }

        this.minute = minute;
        this.litersPerMintute = litersPerMintute;
        this.activityProbability = activityProbability;
    }

    public int getMinute(){
        return minute;
    }

    public double getProbability(){
        return activityProbability;
    }

    public double getLitersPerMinute(){
        return litersPerMintute;
    }

    public double getLitersVolume(){
        return litersPerMintute * 1.0;
    }

    @Override 
    public String toString(){
        return String.format("Minute %d | Caudal: %.2f L/min | probability %.2f", minute, litersPerMintute, activityProbability);
    }
}