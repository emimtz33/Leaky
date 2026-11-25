package Detection;

import java.util.List;

import Model.FlowReading;

public interface DetectionPolicy {

    int getMinutesWindow();

    boolean isAbnormal(List<FlowReading> recentReadings);
    
}
