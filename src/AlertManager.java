import java.util.ArrayList;
import java.util.List;

import Detection.Leak;

public class AlertManager {
    private final List<Leak> alertas =
        new ArrayList<>();

    public void emitir(Leak fuga) {

        alertas.add(fuga);

        System.out.printf(
            "%n*** ALERTA DE FUGA ***%n" +
            "Inicio de sospecha: minuto %d%n" +
            "Deteccion: minuto %d%n" +
            "Duracion anormal: %d minutos%n" +
            "Caudal promedio: %.2f L/min%n",
            fuga.getMinutoInicioSospecha(),
            fuga.getMinutoDeteccion(),
            fuga.getMinutosAnormales(),
            fuga.getPromedioDetectado()
        );
    }

    public List<Leak> obtenerAlertas() {
        return new ArrayList<>(alertas);
    }
}
