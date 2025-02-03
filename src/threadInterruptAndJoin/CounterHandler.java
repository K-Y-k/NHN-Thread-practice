package threadInterruptAndJoin;

public class CounterHandler implements Runnable {
    private final long countMaxSize;
    private long count;

    public CounterHandler(long countMaxSize) {
        if (countMaxSize <= 0) {
            throw new IllegalArgumentException();
        }

        this.countMaxSize = countMaxSize;
        this.count = 0l;
    }

    @Override
    public void run() {
        do {
            // 쓰레드가 interrupt 상태라면 종료
            if (Thread.interrupted()) {
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted during sleep.");
                break;
                //throw new RuntimeException(e);
            }

            count++;
            System.out.printf("thread: %s, count: %d\n", Thread.currentThread().getName(), count);
        } while (count < countMaxSize);
    }
}


class App {
    public static void main(String[] args) {
        CounterHandler counterHandler = new CounterHandler(10l);
        Thread thread = new Thread(counterHandler);
        
        System.out.printf("thread-state: %s\n", thread.getState());

        thread.setName("my-counter");
        
        thread.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // 쓰레드를 중단 되도록 하는 메소드
        thread.interrupt();


        try {
            // 현재 스레드가 다른 스레드의 실행이 끝날 때까지 기다리도록 하는 메소드
            // 즉, 아래 로직 코드들은 해당 쓰레드가 끝날 때까지 대기한다.
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 이 로직 코드느 해당 쓰레드가 끝날 때까지 대기한다.
        System.out.println("Application exit!");
        System.out.printf("thread-state: %s\n", thread.getState());
    }
}