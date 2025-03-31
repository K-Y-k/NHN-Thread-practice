package counterThreadMulti;

public class CounterHandler implements Runnable  {
    private final long countMaxSize;
    private long count;

    public CounterHandler(long countMaxSize) {
        if(countMaxSize <= 0){
            throw new IllegalArgumentException();
        }

        this.countMaxSize = countMaxSize;
        this.count = 0l;
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
            System.out.printf("thread: %s, count: %d\n", Thread.currentThread().getName(), count);
        } while (count < countMaxSize);
    }
}


class App {
    public static void main(String[] args) {
        // CounterHandler 객체를 생성 합니다. countMaxSize : 10
        CounterHandler counterHandler = new CounterHandler(10l);

        // threadA 생성시 counterHandler 객체를 paramter로 전달 합니다.
        Thread threadA = new Thread(counterHandler);

        // threadA의 name을 my-counter로 설정 합니다.
        threadA.setName("my-counter-A");

        // threadB 생성시 counterHandler 객체를 paramter로 전달 합니다.
        Thread threadB = new Thread(counterHandler);

        // threadB의 name을 my-counter로 설정 합니다.
        threadB.setName("my-counter-B");

        // threadA를 시작 합니다.
        threadA.start();

        // threadB를 시작 합니다.
        threadB.start();


        // A와 B의 우선순위가 랜덤으로 진행되는데
        // 이 이유는 현재 쓰레드들에는 우선순위가 따로 부여되지 않았기에
        // 운영체제 OS가 알아서 가져오기 때문이다.
    }
}