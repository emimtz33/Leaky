package Simulation;

import Model.FlowReading;

public interface FlowSource {
    //What should a flow source need to do
    FlowReading getReading();

    void restart();
}