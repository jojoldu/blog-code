import test.ExceptionPerformance;
import test.Performance;
import test.StringPerformance;

public class Main {

    public static void main(String[] args) {
        Performance[] performances = {
                new ExceptionPerformance(),
                new StringPerformance()};

        for(Performance performance : performances){
            performance.execute();
        }
    }
}
