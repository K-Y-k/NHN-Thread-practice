package interrupt;

public class CounterHandler implements Runnable  {
    private final long countMaxSize;

    private long count;

    public CounterHandler(long countMaxSize) {
        if(countMaxSize<=0){
            throw new IllegalArgumentException();
        }

        this.countMaxSize = countMaxSize;
        this.count=0l;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(1000);
                count++;
                System.out.printf("thread: %s, state: %s, count: %d\n", Thread.currentThread().getName(), Thread.currentThread().getState(), count);
            } catch (InterruptedException e) {
                System.out.printf("thread: %s - state: %s - interupted 발생\n", Thread.currentThread().getName(), Thread.currentThread().getState(), count);
                throw new RuntimeException(e);
            }

        // 해당 thread의 isInterrupted() 상태가 false일 경우 무한 루프
        } while (count < countMaxSize && !Thread.currentThread().isInterrupted());

    }
}


class App {
    public static void main(String[] args) {

        // counterHandlerA 객체를 생성합니다. countMaxSize : 10
        CounterHandler counterHandlerA = new CounterHandler(10l);

        // threadA 생성시 counterHandlerA 객체를 paramter로 전달합니다.
        Thread threadA = new Thread(counterHandlerA);

        // threadA의 name을 'my-counter-A'로 설정합니다.
        threadA.setName("my-counter-A");
        System.out.printf("threadA-state: %s\n", threadA.getState());


        // counterHandlerB 객체를 생성합니다. countMaxSize : 10
        CounterHandler counterHandlerB = new CounterHandler(10l);
        
        // threadB 생성시 counterHandlerB 객체를 paramter로 전달합니다.
        Thread threadB = new Thread(counterHandlerB);

        // threadB의 name을 'my-counter-B'로 설정합니다.
        threadB.setName("my-counter-B");
        System.out.printf("threadB-state: %s\n", threadA.getState());


        // threadA를 시작합니다.
        threadA.start();
        System.out.printf("threadA-state: %s\n", threadA.getState());

        // threadB를 시작합니다.
        threadB.start();
        System.out.printf("threadB-state: %s\n", threadA.getState());


        // main Thread 에서 3초 후 threadA에 interrupt 예외를 발생시킵니다.
        try {
            Thread.sleep(3000);

            // interrput 발생
            System.out.println("threadA.interrupt() 호출");
            threadA.interrupt();

            // 2초 후 threadA의 상태가 TERMINATE가 된것을 확인할 수 있습니다.
            Thread.sleep(2000);
            System.out.printf("threadA-state: %s\n", threadA.getState());
        } catch (Throwable e) {
            System.out.printf("exception: %s\n", e);
        }

        // Main Thread가 threadA, ThreadB가 종료될 때 까지 대기합니다.
        do {
            Thread.yield();
        } while (threadA.isAlive() || threadB.isAlive() );

        // threadA, threadB 상태를 출력 합니다.
        System.out.printf("threadA-state: %s\n", threadA.getState());
        System.out.printf("threadB-state: %s\n", threadA.getState());

        // main thread 종료, 'Application exit!' message를 출력합니다.
        System.out.println("Application exit!");
    }
}