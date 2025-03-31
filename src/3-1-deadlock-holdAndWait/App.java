package deadlockOneHoldAndWait;

public class App {

    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public static void main(String[] args) {
        /**
         * start()부분에 sleep을 중간에 걸었기에 순차적으로 진행되는 상황이고
         * 
         * Thread3가 먼저 실행되어 resource2를 먹은 상태이고
         * 이후 Thread2가 실행되어 resource1을 먹고 resource2를 접근하려 하지만
         * Thread3가 이미 먹고 있는 상태라서 풀릴 때까지 점유 대기에 걸린다.
         */

        // Thread-1은 resource1의 접근 권한을 획득하기 위해 대기합니다.
        Thread thread1 = new Thread(() -> {
            // resource1의 접근 권한을 획득합니다.
            synchronized (resource1) {
                System.out.printf("%s : locked resource 1\n", Thread.currentThread().getName());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        });
        thread1.setName("Thread-1");

        // Thread-2는 resource1의 접근 권한을 획득한 상태일 때 resource2의 접근 권한을 대기하고 있습니다.
        Thread thread2 = new Thread(() -> {
            // resource1을 먼저 잠금
            synchronized (resource1) {
                System.out.printf("%s : locked resource 1\n", Thread.currentThread().getName());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }

                // 그 다음 resource2를 잠금
                synchronized (resource2) {
                    System.out.printf("%s : locked resource 2\n", Thread.currentThread().getName());
                }
            }
        });
        thread2.setName("Thread-2");

        // Thread-3은 resource2의 접근 권한을 획득하기 위해 대기합니다.
        Thread thread3 = new Thread(() -> {
            // resource2의 접근 권한을 획득합니다.
            synchronized (resource2) {
                System.out.printf("%s : locked resource 2\n", Thread.currentThread().getName());

                try {
                    while (true) {
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        });
        thread3.setName("Thread-3");


        try {
            thread3.start();
            Thread.sleep(1000);

            thread2.start();
            Thread.sleep(1000);

            thread1.start();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

}
