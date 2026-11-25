package Storage;

import java.util.ArrayList;
import java.util.List;

import Model.FlowReading;

public class LectureInMemory implements LectureRepository {

    private final List<FlowReading> normales = new ArrayList<>();
    private final List<FlowReading> monitoreo = new ArrayList<>();

    @Override
    public void guardarNormal(FlowReading reading) {
        normales.add(reading);
    }

    @Override
    public void guardarMonitoreo(FlowReading reading) {
        monitoreo.add(reading);
    }

    @Override
    public List<FlowReading> obtenerNormales() {
        return new ArrayList<>(normales);
    }

    @Override
    public List<FlowReading> obtenerMonitoreo() {
        return new ArrayList<>(monitoreo);
    }
}
