package counterThread;


// CounterThread는 Thread를 상속합니다.
public class CounterThread extends Thread {
    private final long countMaxSize;

    private long count;

    public CounterThread(String name, long countMaxSize) {
        // name이이 null 이거나 공백 문자열이면 IllegalArgumentException이 발생합니다.
        if(name == null || name.isEmpty() ){
            throw new IllegalArgumentException();
        }

        // countMaxSize <=0 이면 IllegalArgumentException이 발생합니다.
        if(countMaxSize <= 0){
            throw new IllegalArgumentException();
        }

        this.setName(name);
        this.countMaxSize = countMaxSize;
        this.count = 0;
    }

    @Override
    public void run() {
        /* run method를 구현 합니다.
            1초에 한 번식 다음과 같이 출력 됩니다.
            ex) thread:my-thread, count:1
            - count : 1~10 까지 출력 됩니다.
         */
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            count++;
            System.out.printf("thread: %s, count: %d\n", this.getName(), count);
        } while (count < countMaxSize);
    }
}

class App {
    public static void main(String[] args) {
        // CounterThread 객체를 생성 합니다.
        CounterThread counterThread = new CounterThread("my-counter",10);

        // counterThread를 시작 합니다.
        counterThread.start();
    }
}