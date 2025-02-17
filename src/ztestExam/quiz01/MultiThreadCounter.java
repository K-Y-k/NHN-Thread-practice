package ztestExam.quiz01;

public class MultiThreadCounter implements Runnable {
    // 필드를 선언하세요.
    private int localCount;
    // ThreadLocal 활용 방식
    // ThreadLocal<Integer> localCount = new ThreadLocal<>();

    private int globalCount;
    private int maxCount;

    public MultiThreadCounter(int maxCount) {
        // 인수 검증과 필드 초기화를 하세요.
        if (maxCount <= 0) {
            throw new IllegalArgumentException("maxCount > 0");
        }

        this.localCount = 0;
        this.globalCount = 0;
        this.maxCount = maxCount;
    }

    public int getGlobalCount() {
        return globalCount;
    }

    @Override
    public synchronized void run() {
        // 요구 기능을 구현하세요.
        // 쓰레드 실행 시, localCount는 초기화

        // 전역 변수로 선언된 것인데 로컬 변수로 사용 되어 잘못된 형태이다.
        localCount = 0;

        while (localCount < maxCount) {
            localCount++;
            globalCount++;
        }


        // ThreadLocal 활용법
        // 
        // localCount.set(0);
        // while (localCount.get() < maxCount) {
        //     synchronized (this) {
        //         globalCount++;
        //     }
        //
        //     localCount.set(localCount.get() + 1);
        // }
        //
        // localCount.remove();
    }
}
