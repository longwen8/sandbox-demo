public class Test {

    public static void main(String[] args) {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread sleep ...");

        }
    }
}
