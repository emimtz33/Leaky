package Detection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.FlowReading;

public class PolicyPerRecord implements DetectionPolicy{
    private final int minutesWindow;
    private final double averageThreshold;

    public PolicyPerRecord(List<FlowReading> normals, int minutesWindow, double margin) {

        if (normals == null || minutesWindow < 1 || normals.size() < minutesWindow * 3) {
            throw new IllegalArgumentException("Not enough normal record");
        }

        if (!Double.isFinite(margin) || margin < 0) {
            throw new IllegalArgumentException("Invalid Margin");
        }

        this.minutesWindow = minutesWindow;

        List<Double> averages = new ArrayList<>();

        // Calculate average flow for every normal window
        for (int inicio = 0;inicio <= normals.size() - minutesWindow;inicio++) {

            double sum = 0;

            for (int i = inicio;i < inicio + minutesWindow;i++) {
                sum += normals.get(i).getLitersPerMinute();
            }

            averages.add(sum / minutesWindow);
        }

        // Sort averages to calculate percentile 95
        Collections.sort(averages);

        int indice95 = (int) Math.ceil(averages.size() * 0.95) - 1;

        averageThreshold = averages.get(indice95) + margin;
    }


    @Override 
    public int getMinutesWindow(){
        return minutesWindow;
    }

    public double getAverageThreshold(){
        return averageThreshold;
    }

    @Override 
    public boolean isAbnormal(List<FlowReading> recentReadings){
        if (recentReadings.size() != minutesWindow){
            throw new IllegalArgumentException("Wrong size window");
        }

        double sum = 0;

        for (FlowReading reading : recentReadings){
            sum += reading.getLitersPerMinute();
        }

        double average = sum / minutesWindow;

        return average > averageThreshold;
    }

    
}
