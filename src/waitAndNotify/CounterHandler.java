package waitAndNotify;

public class CounterHandler implements Runnable  {
    private final Object monitor;
    private final long countMaxSize;

    private long count;

    public CounterHandler(long countMaxSize, Object monitor) {
        // countMaxSize<=0 or monitor 객체가 null 이면 IllegalArgumentException이 발생 합니다.
        if(countMaxSize<=0){
            throw new IllegalArgumentException();
        }

        // countMaxSize, count, monitor 변수를 초기화 합니다.
        this.countMaxSize = countMaxSize;
        this.count = 0l;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Thread에 의해서 run() method가 호출되면 무한 대기 합니다.
        // monitor는 여러 Thread가 동시에 접근할 수 없도록  접근을 제어해야 합니다.
        synchronized (monitor){
            try {
                monitor.wait(); // wait은 항상 synchronized를 무조건 씌워야 한다.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            count++;
            System.out.printf("thread: %s, state: %s, count: %d\n", Thread.currentThread().getName(), Thread.currentThread().getState(), count);

        } while (count < countMaxSize);
    }
}


class App {

    // monitor로 사용한 객체를 생성합니다.
    public static Object monitor = new Object();

    public static void main(String[] args) {

        // counterHandlerA 객체를 생성합니다. countMaxSize : 10, monitor
        CounterHandler counterHandlerA = new CounterHandler(10l,monitor);

        // threadA 생성시 counterHandlerA 객체를 paramter로 전달합니다.
        Thread threadA = new Thread(counterHandlerA);

        // threadA의 name을 'my-counter-A' 로 설정합니다.
        threadA.setName("my-counter-A");
        System.out.printf("threadA-state: %s\n", threadA.getState());


        // threadA를 시작합니다.
        threadA.start();
        System.out.printf("threadA-state: %s\n", threadA.getState());


        // Main Thread에서 2초 후 monitor를 이용하여 대기하고 있는 threadA를 깨움니다.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        synchronized (monitor){
            System.out.println("call monitor.notify()");
            monitor.notify();
        }

        
        // Main Thread는 threadA가 종료될 때 까지 대기합니다.
        do {
            Thread.yield();
        } while (threadA.isAlive());

        // 'Application exit!' message를 출력합니다.
        System.out.println("Application exit!");
    }

}