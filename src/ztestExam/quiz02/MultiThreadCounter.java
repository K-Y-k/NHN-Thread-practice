package ztestExam.quiz02;

import java.util.concurrent.atomic.AtomicInteger;


public class MultiThreadCounter implements Runnable {
    /**
     * 꼭 ThreadLocal과 Atomic을 사용할 필요는 없다.
     * 상황에 따라 활용하자
     */

    // 필드를 선언하세요.
    private ThreadLocal<Integer> localCount = new ThreadLocal<>();
    private AtomicInteger globalCount = new AtomicInteger(0);
    private int maxCount;

    public MultiThreadCounter(int maxCount) {
        // 인수 검증과 필드 초기화를 하세요.
        if (maxCount <= 0) {
            throw new IllegalArgumentException("maxCount > 0");
        }
        
        this.maxCount = maxCount;
    }

    public int getGlobalCount() {
        return globalCount.get();
    }

    @Override
    public void run() {
        // 요구 기능을 구현하세요.
        localCount.set(0);

        while (localCount.get() < maxCount) {
            localCount.set(localCount.get() + 1);
            globalCount.getAndIncrement();
        }

        // ThreadLocal은 삭제 처리까지 하기
        localCount.remove();
    }
}
