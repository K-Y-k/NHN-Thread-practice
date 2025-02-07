package zmartProgram.main.mart.thread.util;

import java.util.Objects;

public class RequestHandler implements Runnable {

    // 요청 대기열
    private final RequestChannel channel;

    public RequestHandler(RequestChannel channel) {
        // RequestChannel == null 이면 IllegalArgumentException이 발생
        if (Objects.isNull(channel)){
            throw new IllegalArgumentException();
        }

        // channel을 초기화 합니다.
        this.channel = channel;
    }

    
    @Override
    public void run() {
        /*
            interrupt가 발생하면 while문이 종료되면서 thread를 빠져 나가게 됩니다.
         */
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 작업 큐에서 작업을 빼와서 실행합니다.
                Executable executable = channel.getRequest();
                executable.execute();

                Thread.sleep(100);
            } catch (Exception e) {
                // interruptedException이 발생하면 interrupted flag 값이 false 상태로 설정됩니다. 
                // 즉, 'Thread.currentThread().interrupt()'를 호출하여 다시 true상태로 변경합니다.
                // 상위 레벨의 다른 코드 또는 쓰레드가 이 쓰레드가 인터럽트 되었음을 인지 할 수 있습니다.
                if (e.getMessage().contains(InterruptedException.class.getName())) {
                    Thread.currentThread().interrupt();
                }

                // 종료될 떄 필요한 코드가 있다면 작성합니다.
                System.out.printf("RequestHandler error : %s\n",e.getMessage(),e);
            }
        }
    }
}
