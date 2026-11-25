package Storage;

import java.util.List;

import Model.FlowReading;

public interface LectureRepository {

    void guardarNormal(FlowReading lectura);

    void guardarMonitoreo(FlowReading lectura);

    List<FlowReading> obtenerNormales();

    List<FlowReading> obtenerMonitoreo();
    
    
}
