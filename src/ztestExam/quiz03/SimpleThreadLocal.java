package ztestExam.quiz03;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SimpleThreadLocal<T> {
    // Supplier<T>는 매개변수를 받지 않고 T 타입의 객체를 반환하는 역할

    // ThreadLocal의 값은 쓰레드마다 고유해야 하므로, 
    // 각 스레드가 처음 get()을 호출할 때 값이 없다면 supplier.get()을 통해 초기 값을 생성하게 된다. 
    // 즉, 값이 아직 설정되지 않은 쓰레드에 대해 초기값을 생성하는 책임을 맡고 있다.
    Supplier<T> supplier;

    // Map<Thread, T> valueMap = new HashMap<>();
    // 동기화에 유리한 ConcurrentHashMap 활용
    Map<Thread, T> valueMap = new ConcurrentHashMap<>();

    public SimpleThreadLocal(Supplier<T> supplier) {
        // 인수 검증과 필드 초기화를 하세요.
        if (supplier == null) {
            throw new IllegalArgumentException("supplier is not null");
        }

        this.supplier = supplier;


        // 잘못 넣음 
        // 쓰레드마다 값을 설정해야 하는 시점이 아니라, 
        // get() 메서드를 호출했을 때 초기값을 설정해야 하기 때문이다. 
        // 생성자에서 이를 넣으면 모든 스레드가 인스턴스를 생성할 때 supplier.get()을 호출하여 초기 값이 미리 설정됩니다. 
        // 그러나 이는 ThreadLocal의 의미에 맞지 않는다.
        // valueMap.put(Thread.currentThread(), supplier.get());
    }

    public T get() {
        return valueMap.get(Thread.currentThread());
    }

    public T get(Thread thread) {
        // 내가 푼 방식
        // return valueMap.get(thread);

        // 해당 키가 맵에 없으면, 지정된 기본값인 supplier.get()을 반환
        return valueMap.getOrDefault(thread, supplier.get());

        // currentThread()는 static 메소드이므로
        // thread.currentThread()와 Thread.currentThread()와 동일하다.
        // 즉, thread.currentThread()와 같이 인스턴스 메소드처럼 보이기 보다는 
        //     Thread.currentThread()인 static 메소드로 사용하자.
    }

    public void set(T value) {
        valueMap.put(Thread.currentThread(), value);
    }

    public void remove() {
        valueMap.remove(Thread.currentThread());
    }

    public void remove(Thread thread) {
        valueMap.remove(thread);
    }
}
