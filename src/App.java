import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Random random = new Random();

        for (int i = 0; i < 5; i++){
            System.out.println(random.nextDouble() );
        }
        
    }
}
