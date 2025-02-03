package runnable;

// Runnable interface을 구현 합니다.
public class CounterHandler implements Runnable  {
    private final long countMaxSize;
    private long count;

    public CounterHandler(long countMaxSize) {
        // countMaxSize <=0 이면 IllegalArgumentException()이 발생합니다.
        if (countMaxSize <= 0){
            throw new IllegalArgumentException();
        }

        this.countMaxSize = countMaxSize;
        this.count = 0l;
    }


    @Override
    public void run() {
        /* Runnable 인터페이스의 run 메소드를 구현합니다.
             - 1초에 한 번식 다음과 같이 출력 됩니다.
             - count 1 ~ 10 까지 출력 됩니다.
            ex) thread:my-thread,count:1 ....
         */
        do {
            try {
                Thread.sleep(1000);
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
        // CounterHandler 객체를 생성 합니다. countMaxSize : 10
        CounterHandler counterHandler = new CounterHandler(10l);

        // thread 생성시 counterHandler 객체를 paramter로 전달합니다.
        Thread thread = new Thread(counterHandler);

        // thread의 이름을 my-counter로 설정합니다.
        thread.setName("my-counter");

        // thread를 시작합니다.
        // thread의 run()   메소드는 현재 쓰레드의 상태를 같이 가져오는 것이 아닌 현재 쓰레드에서 구현한 run()만 실행되고
        // thread의 start() 메소드는 현재 쓰레드의 상태를 가져오면서 현재 쓰레드에서 구현한 run()을 실행한다.
        thread.start();
    }
}
