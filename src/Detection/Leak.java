package Detection;

public class Leak {
    
    private final int susMinutes;
    private final int detectionMinute;
    private final int abnormalMintues;
    private final double detectedAverage;

    public Leak(int susMinutes,int detectionMinute,int abnormalMintues,double detectedAverage) {

        this.susMinutes = susMinutes;
        this.detectionMinute = detectionMinute;
        this.abnormalMintues = abnormalMintues;
        this.detectedAverage = detectedAverage;
    }

    public int getMinutoInicioSospecha() {
        return susMinutes;
    }

    public int getMinutoDeteccion() {
        return detectionMinute;
    }

    public int getMinutosAnormales() {
        return abnormalMintues;
    }

    public double getPromedioDetectado() {
        return detectedAverage;
    }
}
