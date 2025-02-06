package deadlockTwoCircularWait;

public class App {
    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public static void main(String[] args) {
       /**
         * sleep을 중간에 두지 않고 start()하므로
         * Thread1과 Thread2는 동시에 실행되고
         * 
         * Thread1이 resource2를 먹을 때
         * Thread2도 resource1을 먹고
         * 
         * 이후 Thread1은 resource1을 접근하려 하고
         * Thread2도 resource2에 접근하려는데 이미 해당 자원들은 먹고 있는 상황으로
         * 순환과 대기 상태가 된다.
         */

        // Thread-1가 resource2의 접근 권한을 획득한 상태에서 resource1의 접근 권한을 대기하고 있습니다.
        Thread thread1 = new Thread(() -> {
            // resource2를를 먼저 잠금
            synchronized (resource2) {
                System.out.printf("%s : locked resource 2\n", Thread.currentThread().getName());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }

                // 그 다음 resource1을 잠금
                synchronized (resource1) {
                    System.out.printf("%s : locked resource 1\n", Thread.currentThread().getName());
                }
            }
        });
        thread1.setName("Thread-1");

        // Thread-2가 resource1의 접근 권한을 획득한 상태에서 resource2의 접근 권한을 대기하고 있습니다.
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


        thread1.start();
        thread2.start();
    }
}
