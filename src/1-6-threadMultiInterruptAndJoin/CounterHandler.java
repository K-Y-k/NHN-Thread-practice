package threadMultiInterruptAndJoin;

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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            count++;
            System.out.printf("thread: %s, count: %d\n", Thread.currentThread().getName(), count);
        } while (count < countMaxSize);
    }
}


class App {
    public static void main(String[] args) {
        // counterHandlerA 객체를 생성합니다. countMaxSize : 10
        CounterHandler counterHandlerA = new CounterHandler(10l);
        
        // threadA 생성시 counterHandlerA 객체로 전달합니다.
        Thread threadA = new Thread(counterHandlerA);
        
        // threadA의 name을 'my-counter-A'로 설정합니다.
        threadA.setName("my-counter-A");
        System.out.printf("threadA-state: %s\n", threadA.getState());


        // counterHandlerB 객체를 생성 합니다. countMaxSize : 10
        CounterHandler counterHandlerB = new CounterHandler(10l);
        
        // threadB 생성시 counterHandlerB 객체로 전달합니다.
        Thread threadB = new Thread(counterHandlerB);
        
        // threadB의 name을 'my-counter-B' 로 설정합니다.
        threadB.setName("my-counter-B");
        System.out.printf("threadB-state: %s\n", threadB.getState());


        // threadA를 시작 합니다.
        threadA.start();

        try {
            // threadA 작업이 완료될 때까지 main Thread는 대기합니다.
            threadA.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.printf("threadA-state: %s\n", threadA.getState());


        // threadB를 시작 합니다.
        threadB.start();

        try {
            // threadB 작업이 완료될 때까지 main Thread는 대기합니다.
            threadB.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("threadB-state: %s\n", threadB.getState());


        // 'Application exit!' message를 출력 합니다.
        System.out.println("Application exit!");
    }
}