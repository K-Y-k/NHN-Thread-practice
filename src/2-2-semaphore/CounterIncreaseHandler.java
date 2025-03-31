package semaphore;

import java.util.Objects;

public class CounterIncreaseHandler implements Runnable {
    private final SharedCounter sharedCounter;

    public CounterIncreaseHandler(SharedCounter sharedCounter) {
        // sharedCounter를 초기화 합니다.  
        // sharedCounter가 null 이면 IllegalArgumentException이 발생합니다.
        if (Objects.isNull(sharedCounter)){
            throw new IllegalArgumentException(String.format("SharedCount is null"));
        }
        this.sharedCounter = sharedCounter;
    }

    @Override
    public void run() {
        // 현재 Thread의 interrupted이 ture <--  while의 종료조건 : interrupt가 발생 했다면 종료합니다.
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);

                // sharedCounter의 count를 1증가 시키고 count값을 반환합니다.
                long count = sharedCounter.increaseAndGet();
                System.out.printf("thread: %s, count: %d\n", Thread.currentThread().getName(), count);
            } catch (Exception e) {
                System.out.printf("thread: %s - interrupt!\n",Thread.currentThread().getName());
                
                // 현재 Thread에 interrupt()를 호출하여 interrput()를 발생시킵니다. 
                // 즉 현제 Thread의 interrupted 값이 -> true로 변경 됩니다. -> 즉 while 문을 종료하게 됩니다.
                Thread.currentThread().interrupt();
            }
        }
    }
}
