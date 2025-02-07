package zmartProgram.main.mart.thread.util;

import java.util.LinkedList;
import java.util.Queue;

public class RequestChannel {

    // Executable 타입을 담을 Queue
    private final Queue<Executable> requestQueue;

    // 기본 큐 Size
    private static final long DEFAULT_QUEUE_SIZE = 10;

    // 큐 size
    private final long queueSize;

    public RequestChannel() {
        // 기본생성자로 DEFAULT_QUEUE_SIZE 기반으로 QUEUE를 생성합니다.
        this(DEFAULT_QUEUE_SIZE);
    }

    public RequestChannel(long queueSize) {
        // queueSize < 0 이면 IllegalArgumentException 발생
        if (queueSize < 0) {
            throw new IllegalArgumentException();
        }

        // queueSize, requestQueue를 초기화 합니다.
        this.requestQueue = new LinkedList<>();
        this.queueSize = queueSize;
    }

    public synchronized void addRequest(Executable executable) {
        // 현재 작업 큐 크기가 최대 크기로 모두 차 있으면 대기합니다.
        while (requestQueue.size() >= queueSize){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // 작업 큐에 작업을 추가하고 대기하고 있는 thread를 깨웁니다.
        requestQueue.add(executable);
        notifyAll();
    }

    public synchronized Executable getRequest() {
        // 직압 큐가 비어 있다면 대기합니다.
        while (requestQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // 작업 큐에서 작업을 반환하고  대기하고 있는 thread를 깨웁 니다.
        notifyAll();
        return requestQueue.poll();
    }
}
