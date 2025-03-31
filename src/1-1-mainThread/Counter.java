package mainThread;

public class Counter {
    private final long countMaxSize;
    private long count;

    public Counter(long countMaxSize) {

        if(countMaxSize <=0){
            throw new IllegalArgumentException();
        }

        this.countMaxSize = countMaxSize;
        this.count = 0l;
    }

    public void run() {
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            count++;
            System.out.printf("name: %s, count: %d\n", Thread.currentThread().getName(), count);
        } while (count<countMaxSize);
    }
}


class App {
    public static void main( String[] args ) {
        // 프로그램을 실행하면 하나의 쓰레드가 자동 생성되고 즉시 실행되기 시작하는데
        // 이 쓰레드를 Main Thread라고 한다.

        // 생성된 쓰레드를 제어하기 위해서는 currentThread()의 메서드들을 호출하면 
        // 현재 Thread의 참조를 얻을수 있다.
        // 현재 쓰레드인 Main 쓰레드의 이름을 설정
        Thread.currentThread().setName("my-thread");

        // 위에 구현한 카운터 클래스 생성 및 사용
        Counter counter = new Counter(10);
        counter.run();
    }
}