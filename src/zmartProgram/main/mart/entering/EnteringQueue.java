package zmartProgram.main.mart.entering;

import java.util.LinkedList;
import java.util.Queue;

import zmartProgram.main.customer.domain.Customer;

public class EnteringQueue {

    // Queue를 이용해서 mart 입장 대기열을 구현
    private final Queue<Customer> queue;

    // 기본 대기열 QueueSize = 100명
    private static final int DEFAULT_CAPACITY = 100;
    // 최대 수용자 필드
    private final int capacity;

    public EnteringQueue() {
        // 기본 생성자 구현, capacity = DEFAULT_CAPACITY
        this(DEFAULT_CAPACITY);
    }

    public EnteringQueue(int capacity) {
        // capacity <=0 IllegalArgumentException이 발생합니다.
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        // capacity와 queue를 초기화 합니다.
        this.capacity = capacity;
        queue = new LinkedList<>();
    }

    public synchronized void addCustomer(Customer customer) {
        /*
         * 대기열에 고객을 추가하는 method
           - queue.size() >= capacity 이면 대기할 수 있도록 구현
         */
        while (queue.size() >= capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // 큐에 고객을 추가하고 대기하고 있는 Thread를 깨웁니다.
        queue.add(customer);
        notifyAll();
    }

    public synchronized Customer getCustomer() {
        // 큐가 비워져 있다면 대기합니다.
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // 큐에서 customer를 반납하고 대기하고 있던 thread를 깨웁니다.
        notifyAll();
        return queue.poll();
    }

    // 큐 size를 반환합니다.
    public int getQueueSize(){
        return queue.size();
    }
}