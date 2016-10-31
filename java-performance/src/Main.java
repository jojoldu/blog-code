public class Main {

    public static void main(String[] args) {
        Performance[] performances = {
                new ExceptionPerformance()};

        for(int i=0; i<5; i++){
            for(Performance performance : performances){
                performance.execute();
            }
        }
    }
}
