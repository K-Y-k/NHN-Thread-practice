package deadlockThreeStarvation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CounterIncreaseHandler implements Runnable {
    private final SharedCounter sharedCounter;

    private final List<String> threadHistory;

    public CounterIncreaseHandler(SharedCounter sharedCounter) {
        // sharedCounter가 null 이면 IllegalArgumentException이 발생합니다.
        if (Objects.isNull(sharedCounter)){
            throw new IllegalArgumentException(String.format("SharedCount is null"));
        }

        //sharedCounter를 초기화 합니다.
        this.sharedCounter = sharedCounter;
        this.threadHistory = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void run() {
        // 현재 Thread에 interrupt가 발생했다면 종료합니다.
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                
                // sharedCounter의 count를 1증가 시키고 count값을 반환합니다.
                long count = sharedCounter.increaseAndGet();

                // A ~ E Thread가 공유자원을 사용한 횟수를 맵에 등록합니다.
                threadHistory.add(Thread.currentThread().getName());
                System.out.printf("thread: %s, count: %d\n", Thread.currentThread().getName(), count);

                // A ~ E thread 중 먼저 실행하는 Thread가 무한 점유합니다. 
                // 즉 다른 Thread는 increaseAndGet()를 실행 할 수 없습니다.
                synchronized (this) {
                    while (true) {
                        System.out.printf("%s is working\n", Thread.currentThread().getName());
                    }
                }
            } catch (Exception e) {
                System.out.printf("%s - interrupt!\n", Thread.currentThread().getName());

                // 현재 Thread에 interrupt()를 호출하여 interrput()를 발생 시킵 니다. 즉 현제 Thread의 interrupted 값이 -> true로 변경 됩니다. -> 즉 while 문을 종료하게 됩니다.
                Thread.currentThread().interrupt();
            }
        }
    }

    public Map<String, Long> getThreadHistory() {
        return threadHistory.stream().collect(Collectors.groupingBy(s->s,Collectors.counting()));
    }

}
