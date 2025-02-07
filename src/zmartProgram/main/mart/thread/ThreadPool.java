package zmartProgram.main.mart.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThreadPool {

    // thread-pool 기본 크기 = 10
    private static final int DEFAULT_POOL_SIZE = 10;

    // thread pool size
    private final int poolSize;

    // thread에 의해서 실행될 Runnable 구현체
    private final Runnable runnable;

    //t hread-pool에 생성된 thread 리스트
    private final List<Thread> threadList;

    public ThreadPool(Runnable runnable){
        // 기본 생성자 구현, poolSize = DEFAULT_POOL_SIZE를 사용
        this(DEFAULT_POOL_SIZE,runnable);
    }

    public ThreadPool(int poolSize, Runnable runnable) {
        // thread pool size < 0 이면 IllegalArgumentException이 발생
        if(poolSize<0){
            throw new IllegalArgumentException();
        }

        // runable == null 이면 IllegalArgumentException 발생
        if (Objects.isNull(runnable)){
            throw new IllegalArgumentException();
        }

        // runnable 이 Runnable의 구현체가 아니라면 IllegalArgumentException 발생
        if(!(runnable instanceof Runnable)){
            throw new IllegalArgumentException();
        }

        //poolSize, runnable, threadList 초기화
        this.poolSize = poolSize;
        this.runnable = runnable;
        threadList = new ArrayList<>(poolSize);

        // thread를 미리 poolSize만큼 생성합니다.
        createThread();
    }

    private void createThread() {
        synchronized (this) {
            for (int i = 0; i < poolSize; i++) {
                threadList.add(new Thread(runnable));
            }
        }
    }

    public synchronized void start() {
        /* 생성된 thread를 시작 합니다. */
        for (int i = 0; i < poolSize; i++){
            Thread thread = threadList.get(i);
            thread.start();
        }
    }

    public synchronized void stop(){
        /*
            interrupt()를 실행해서 thread를 종료합니다.
            - thread가 종료되는 과정에서 동기화 되어야 합니다.
            - 우선 모든 thread interrupt 호출
         */

        for (Thread thread : threadList) {
            if (Objects.nonNull(thread) && thread.isAlive()) {
                thread.interrupt();
            }
        }

        // 모든 thread가 종료될 떄 까지 대기 상태로 만듭니다.
        for (Thread thread : threadList) {
            try {
                // join method는 해당 thread가 종료될 때 까지 현재 thread를 대기상태로 만듭니다.
                thread.join();
            } catch (InterruptedException e) {
                // join method는 InterruptedException을 발생시킬 수 있습니다.
                Thread.currentThread().interrupt();
                System.out.printf("%s\n", e.getMessage(), e);
            }
        }
    }
}