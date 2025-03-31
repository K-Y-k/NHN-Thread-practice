package deadlockFourLivelock;

public class App {
    public static void main(String[] args)  {
        /**
         * 동시에 시작하므로 
         * 둘 중 하나가 계속 먹고
         * 다른 하나는 무한 대기하게 된다.
         */

        Counter counter = new Counter();

        Runnable task = () -> {
            while (counter.getCount() < 10) {
                counter.increment();
            }
        };

        Thread threadA = new Thread(task, "counter-A");
        Thread threadB = new Thread(task, "counter-B");

        threadA.start();
        threadB.start();
    }

}
