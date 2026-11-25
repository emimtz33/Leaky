package Detection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Optional;

import Model.FlowReading;

public class LeakDetector {

    private final DetectionPolicy politica;
    private final int limiteMinutos;

    private final Deque<FlowReading> ventana = new ArrayDeque<>();

    private int minutosAnormales = 0;
    private int ultimoMinuto = 0;
    private boolean alertaActiva = false;

    public LeakDetector(DetectionPolicy politica,int limiteMinutos) {

        if (politica == null || limiteMinutos < 1) {
            throw new IllegalArgumentException(
                "Configuracion invalida"
            );
        }

        this.politica = politica;
        this.limiteMinutos = limiteMinutos;
    }

    public Optional<Leak> procesar(FlowReading lectura) {

        // Verify chronological order
        if (lectura.getMinute() <= ultimoMinuto) {
            throw new IllegalArgumentException(
                "Lecturas fuera de orden"
            );
        }

        // Reset continuity if a minute is missing
        if (ultimoMinuto != 0 &&
                lectura.getMinute() != ultimoMinuto + 1) {

            ventana.clear();
            minutosAnormales = 0;
            alertaActiva = false;
        }

        ultimoMinuto = lectura.getMinute();

        // Add the newest reading
        ventana.addLast(lectura);

        // Keep only the required readings
        if (ventana.size() > politica.getMinutesWindow()) {
            ventana.removeFirst();
        }

        // Not enough readings yet
        if (ventana.size() < politica.getMinutesWindow()) {
            return Optional.empty();
        }

        boolean anormal = politica.isAbnormal(
            new ArrayList<>(ventana)
        );

        if (anormal) {

            minutosAnormales++;

            if (minutosAnormales > limiteMinutos
                    && !alertaActiva) {

                alertaActiva = true;

                double promedio = ventana.stream()
                    .mapToDouble(
                        FlowReading::getLitersPerMinute
                    )
                    .average()
                    .orElse(0);

                Leak fuga = new Leak(
                    lectura.getMinute() - minutosAnormales + 1,
                    lectura.getMinute(),
                    minutosAnormales,
                    promedio
                );

                return Optional.of(fuga);
            }

        } else {

            minutosAnormales = 0;
            alertaActiva = false;
        }

        return Optional.empty();
    }
}