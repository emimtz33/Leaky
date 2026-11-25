import java.util.Optional;

import Model.FlowReading;
import Simulation.*;
import Storage.LectureInMemory;
import Storage.LectureRepository;
import Detection.*;

public class Main {
    public static void main(String[] args) {
        
        LectureRepository repository = new LectureInMemory();
       // 2. Train with normal consumption
        FlowSource normalSource = new FlowSimulator();

        for (int i = 0; i < 2880; i++) {

            FlowReading reading = normalSource.getReading();

            repository.guardarNormal(reading);
        }

        // 3. Create detection policy
        PolicyPerRecord policy =
            new PolicyPerRecord(
                repository.obtenerNormales(),
                10,     // Moving average: 10 minutes
                1.5     // Tolerance margin
            );

        // 4. Create leak detector
        LeakDetector detector =
            new LeakDetector(
                policy,
                60      // Alert after more than 60 minutes
            );

        // 5. Create alert manager
        AlertManager alerts = new AlertManager();

        // 6. Create a NEW simulator for monitoring
        FlowSource source = new FlowSimulator();

        System.out.printf(
            "Normal threshold: %.2f L/min%n",
            policy.getAverageThreshold()
        );

        /*
        for (int i = 0; i<120;i++){
            FlowReading reading = source.getReading(); //Asks for a reading and stores it inside reading object
            WaterActivity activity = simulator.getCurrentActivity();

            record.add(reading);

            totalVolume += reading.getLitersVolume();

            System.out.println("\n" + reading);
            System.out.println(activity);

            if (reading.getLitersPerMinute() > 0) {minutesWithFlow++;} 
            else {minutesWithoutFlow++;}

            if (activity != null){
                numberOfActivities++;
            }
        }
            */

        for (int i = 0; i < 180; i++) {

            FlowReading normal = source.getReading();

            // Inject artificial leak from minute 30 to 150
            double extraLeak = 0;

            if (normal.getMinute() >= 30 &&
                normal.getMinute() <= 150) {

                extraLeak = 20.0;
            }

            // Total observable flow
            FlowReading observed = new FlowReading(
                normal.getMinute(),
                normal.getLitersPerMinute() + extraLeak,
                normal.getProbability()
            );

            // Save monitoring data
            repository.guardarMonitoreo(observed);

            // Analyze the reading (Optional checks if there is a leak object inside it)
            Optional<Leak> result = detector.procesar(observed);

            // Notify only when a new leak is detected (If a leak is present in it)
            result.ifPresent(alerts::emitir);
        }

        /*
        System.out.printf("\nVolume consumed %.2f liters %n", totalVolume);
        System.out.println("Registred readings: " + record.size());
        System.out.println("Minutes with flow " + minutesWithFlow);
        System.out.println("Minutes without flow " + minutesWithoutFlow);
        System.out.println("Number of Activities " + numberOfActivities);
        */

        System.out.println(
            "Registered readings: " +
            repository.obtenerMonitoreo().size()
        );

        System.out.println(
            "Detected leaks: " +
            alerts.obtenerAlertas().size()
        );
    }
}