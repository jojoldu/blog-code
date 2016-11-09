import test.ExceptionPerformance;
import test.Performance;
import test.StreamPerformance;
import test.StringPerformance;

public class Main {

    public static void main(String[] args) {
        Performance[] performances = {
                new ExceptionPerformance(),
                new StringPerformance(),
                new StreamPerformance()};

        for(Performance performance : performances){
            performance.execute();
            System.out.println("\n\n");
        }
    }
}
