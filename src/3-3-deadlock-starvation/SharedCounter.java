package deadlockThreeStarvation;

public class SharedCounter {
    private long count;

    public SharedCounter(){
        this(0l);
    }

    public SharedCounter(long count) {
        // 생성자를 초기화 합니다. 
        if(count < 0){
            throw new IllegalArgumentException("count > 0 ");
        }

        this.count = count;
    }

    // mehtod 단위 lock을 걸고, count를 반환합니다.
    public synchronized long getCount(){
        return count;
    }

    public long increaseAndGet(){
        // block 단위로 lock을 걸고 1 증가시키고 count를 반환합니다.
        synchronized (this) {
            count = count + 1;
            return count;
        }
    }

    public long decreaseAndGet(){
        // block 단위로 lock을 걸고  1 감소시키고 count를 반환합니다.
        synchronized (this) {
            count = count - 1;
        }
        return count;
    }
}
