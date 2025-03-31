package yield;

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
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            count++;
            System.out.printf("thread: %s, state: %s, count: %d\n", Thread.currentThread().getName(), Thread.currentThread().getState(), count);

            //TODO#2 Thread.yield()를 사용해서 수행되고 있는 작업을 다른 Thread에게 양보 하세요.
            Thread.yield();
        }while (count<countMaxSize);
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


        // Main Thread가 threadA, ThreadB가 종료될 때 까지 대기합니다.
        do {
            Thread.yield();
        } while (threadA.isAlive() || threadB.isAlive());


        // while문과 쓰레드 상태 비교한 방식
        // while (threadA.getState() != Thread.State.TERMINATED && !threadB.getState().equals(Thread.State.TERMINATED))  {
        //     Thread.yield();
        // }

        // join으로 대기한 방식
        // threadA.join();
        // threadB.join();

        //'Application exit!' message를 출력합니다.
        System.out.println("Application exit!");
    }
}
